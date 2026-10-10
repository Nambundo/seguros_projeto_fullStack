import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
});

/**
 * Converte um erro do Axios em { mensagem, campos }.
 * O backend responde: { status, erro, campos? }.
 */
export function mensagemDeErro(err) {
  if (!err.response) {
    return {
      mensagem: 'Não foi possível conectar ao backend. Verifique se ele está rodando em http://localhost:8080.',
      campos: {},
    };
  }
  const corpo = err.response.data || {};
  return {
    mensagem: corpo.erro || `Erro inesperado (HTTP ${err.response.status}).`,
    campos: corpo.campos || {},
  };
}

export default api;
