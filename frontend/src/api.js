async function request(path, options) {
  const response = await fetch(path, options);
  if (!response.ok) {
    let message = `Request failed (${response.status})`;
    try {
      const body = await response.json();
      message = body.message || body.error || message;
    } catch {
      // Keep the HTTP status message when the server does not return JSON.
    }
    throw new Error(message);
  }
  return response.json();
}

export function getPosts() {
  return request("/api/posts?pageNo=0&pageSize=50&sorting=id");
}

export function uploadMedia(file) {
  const formData = new FormData();
  formData.append("file", file);
  return request("/api/uploads", { method: "POST", body: formData });
}

export function createPost(post) {
  return request("/api/posts", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(post),
  });
}
