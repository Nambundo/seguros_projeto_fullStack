export default function SearchBox({ value, onChange }) {
  return (
    <input
      className="search"
      type="search"
      placeholder="Buscar..."
      value={value}
      onChange={(e) => onChange(e.target.value)}
    />
  );
}
