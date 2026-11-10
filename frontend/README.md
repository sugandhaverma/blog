# Fieldnotes frontend

## Run locally

1. Set the `DB_PASSWORD` environment variable to your MySQL password. In Command Prompt, use `set DB_PASSWORD=your_mysql_password` in the same terminal where you start the backend. In PowerShell, use `$env:DB_PASSWORD = "your_mysql_password"`.
2. Start the Spring Boot API from the repository root with `.\mvnw.cmd spring-boot:run`.
3. In this directory, install dependencies with `npm install`.
4. Start the React development server with `npm run dev`.

The Vite development server proxies `/api` and `/uploads` to the API at `http://localhost:8081`. The API still requires the configured MySQL database.

Posts use the existing `/api/posts` endpoints. Optional media is uploaded first to `POST /api/uploads` as multipart form data under the `file` field; the returned URL and media type are then saved on the post. The backend stores files in the local `uploads/` directory by default. Set `UPLOAD_DIR` to change that location. Supported formats are JPEG, PNG, GIF, WebP, MP4, WebM, and MOV; the upload limit is 100 MB per file.
