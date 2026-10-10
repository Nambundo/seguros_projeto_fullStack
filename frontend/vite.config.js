import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  // Porta fixa: o CORS do backend libera http://localhost:5173
  server: { port: 5173, strictPort: true },
});
