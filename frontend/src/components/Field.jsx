/** Rótulo + controle + mensagem de erro do campo (vinda do backend). */
export default function Field({ label, erro, children }) {
  return (
    <label className={`field${erro ? ' field-erro' : ''}`}>
      <span>{label}</span>
      {children}
      {erro && <small>{erro}</small>}
    </label>
  );
}
