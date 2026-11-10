import { useEffect, useState } from "react";
import { createPost, getPosts, uploadMedia } from "./api.js";

const initialForm = { title: "", description: "", content: "" };

function formatDate(id) {
  return `STORY ${String(id).padStart(3, "0")}`;
}

function App() {
  const [posts, setPosts] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [file, setFile] = useState(null);
  const [preview, setPreview] = useState("");
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");

  useEffect(() => {
    let active = true;
    getPosts()
      .then((result) => {
        if (active) setPosts(result.content ?? []);
      })
      .catch((requestError) => {
        if (active) setError(`Could not load posts. ${requestError.message}`);
      })
      .finally(() => {
        if (active) setLoading(false);
      });
    return () => {
      active = false;
    };
  }, []);

  useEffect(() => {
    if (!file) {
      setPreview("");
      return undefined;
    }
    const objectUrl = URL.createObjectURL(file);
    setPreview(objectUrl);
    return () => URL.revokeObjectURL(objectUrl);
  }, [file]);

  function updateForm(event) {
    setForm((current) => ({ ...current, [event.target.name]: event.target.value }));
  }

  function selectFile(event) {
    const selected = event.target.files?.[0] ?? null;
    setError("");
    setNotice("");
    if (selected && !selected.type.startsWith("image/") && !selected.type.startsWith("video/")) {
      setFile(null);
      setError("Choose an image or video file.");
      event.target.value = "";
      return;
    }
    if (selected && selected.size > 100 * 1024 * 1024) {
      setFile(null);
      setError("The file must be smaller than 100 MB.");
      event.target.value = "";
      return;
    }
    setFile(selected);
  }

  async function submitPost(event) {
    event.preventDefault();
    setSubmitting(true);
    setError("");
    setNotice("");
    try {
      const media = file ? await uploadMedia(file) : null;
      const created = await createPost({
        ...form,
        mediaUrl: media?.url ?? null,
        mediaType: media?.mediaType ?? null,
      });
      setPosts((current) => [created, ...current]);
      setForm(initialForm);
      setFile(null);
      setNotice("Your story is published.");
    } catch (requestError) {
      setError(`Could not publish the story. ${requestError.message}`);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <main>
      <header className="topbar">
        <a className="wordmark" href="#home" aria-label="Fieldnotes home">
          fieldnotes<span>.</span>
        </a>
        <nav aria-label="Main navigation">
          <a href="#stories">Stories</a>
          <a className="nav-cta" href="#write">Write a story <span aria-hidden="true">↗</span></a>
        </nav>
      </header>

      <section className="hero" id="home">
        <div className="hero-copy">
          <p className="eyebrow"><span className="eyebrow-line" /> A journal for the curious</p>
          <h1>Little moments.<br /><em>Lasting stories.</em></h1>
          <p className="hero-description">
            A home for the things you notice, the places you go, and the stories worth keeping.
          </p>
          <a className="text-link" href="#stories">Read the latest <span aria-hidden="true">↓</span></a>
        </div>
        <div className="hero-art" aria-label="Decorative illustration of a sun over a landscape">
          <div className="sun" />
          <div className="hill hill-back" />
          <div className="hill hill-front" />
          <span className="art-caption">TAKE THE LONG WAY HOME</span>
        </div>
        <div className="hero-index">01 <span /> 03</div>
      </section>

      <section className="stories-section" id="stories">
        <div className="section-heading">
          <div>
            <p className="eyebrow">From the journal</p>
            <h2>Recent stories</h2>
          </div>
          <p className="section-note">Notes from here, there, and everywhere.</p>
        </div>

        {loading && <p className="state-message">Gathering stories…</p>}
        {!loading && error && <p className="state-message error-message" role="alert">{error}</p>}
        {!loading && !error && posts.length === 0 && (
          <div className="empty-state">
            <span className="empty-mark">✳</span>
            <h3>A fresh page</h3>
            <p>No stories yet. Be the first to add one.</p>
            <a className="text-link" href="#write">Write a story <span aria-hidden="true">↗</span></a>
          </div>
        )}
        <div className="story-grid">
          {posts.map((post) => (
            <article className="story-card" key={post.id}>
              {post.mediaUrl ? (
                post.mediaType === "video" ? (
                  <video className="story-media" src={post.mediaUrl} controls preload="metadata">
                    Your browser does not support video.
                  </video>
                ) : (
                  <img className="story-media" src={post.mediaUrl} alt={post.title} loading="lazy" />
                )
              ) : (
                <div className={`story-placeholder story-scene-${Number(post.id) % 4}`} aria-hidden="true">
                  <span className="scene-sun" />
                  <span className="scene-cloud scene-cloud-one" />
                  <span className="scene-cloud scene-cloud-two" />
                  <span className="scene-hill scene-hill-back" />
                  <span className="scene-hill scene-hill-front" />
                  <span className="scene-tree scene-tree-one" />
                  <span className="scene-tree scene-tree-two" />
                  <span className="scene-tree scene-tree-three" />
                  <span className="scene-spark scene-spark-one">✦</span>
                  <span className="scene-spark scene-spark-two">✦</span>
                </div>
              )}
              <div className="story-meta"><span>{formatDate(post.id)}</span><span>•</span><span>JOURNAL</span></div>
              <h3>{post.title}</h3>
              {post.description && <p className="story-description">{post.description}</p>}
              <p className="story-content">{post.content}</p>
            </article>
          ))}
        </div>
      </section>

      <section className="write-section" id="write">
        <div className="write-intro">
          <p className="eyebrow"><span className="eyebrow-line" /> Your turn</p>
          <h2>Put it<br /><em>into words.</em></h2>
          <p>Every good story starts somewhere. Share yours with a photo or a video from the moment.</p>
          <span className="write-spark" aria-hidden="true">✳</span>
        </div>
        <form className="post-form" onSubmit={submitPost}>
          <label>
            <span>Story title</span>
            <input name="title" value={form.title} onChange={updateForm} placeholder="Give your story a name" required maxLength={200} />
          </label>
          <label>
            <span>A short introduction</span>
            <input name="description" value={form.description} onChange={updateForm} placeholder="What is this story about?" required maxLength={500} />
          </label>
          <label>
            <span>Your story</span>
            <textarea name="content" value={form.content} onChange={updateForm} placeholder="Start writing here…" rows={5} required />
          </label>
          <label className="upload-field">
            <span>Bring it to life <small>Optional · Image or video, up to 100 MB</small></span>
            <span className="upload-control">
              <input type="file" accept="image/jpeg,image/png,image/gif,image/webp,video/mp4,video/webm,video/quicktime" onChange={selectFile} />
              <span className="upload-button">＋ Choose media</span>
              <span className="file-name">{file ? file.name : "JPG, PNG, GIF, WebP, MP4, WebM, MOV"}</span>
            </span>
          </label>
          {preview && (
            <div className="preview-wrap">
              {file.type.startsWith("video/") ? (
                <video src={preview} controls className="preview-media" />
              ) : (
                <img src={preview} alt="Selected media preview" className="preview-media" />
              )}
            </div>
          )}
          {error && <p className="form-message error-message" role="alert">{error}</p>}
          {notice && <p className="form-message success-message" role="status">{notice}</p>}
          <button className="submit-button" type="submit" disabled={submitting}>
            {submitting ? "Publishing…" : "Publish your story"} <span aria-hidden="true">↗</span>
          </button>
        </form>
      </section>

      <footer>
        <a className="wordmark" href="#home">fieldnotes<span>.</span></a>
        <p>Made for the moments that matter.</p>
        <a href="#home" className="back-top">Back to top ↑</a>
      </footer>
    </main>
  );
}

export default App;
