export const moeda = (v) =>
  Number(v ?? 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

// "2026-09-28" -> "28/09/2026" (sem passar por Date, evita bug de fuso horário)
export const data = (iso) => (iso ? iso.split('-').reverse().join('/') : '');

export const cpfMask = (cpf) =>
  cpf && /^\d{11}$/.test(cpf) ? cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4') : cpf;

export const STATUS_APOLICE = ['ATIVA', 'CANCELADA', 'VENCIDA'];
export const STATUS_SINISTRO = ['ABERTO', 'EM_ANALISE', 'APROVADO', 'NEGADO'];
export const TIPOS_SEGURO = ['Auto', 'Residencial', 'Vida', 'Empresarial', 'Saúde'];

export const rotuloStatus = (s) =>
  ({ EM_ANALISE: 'Em análise' }[s] || (s ? s.charAt(0) + s.slice(1).toLowerCase() : ''));
