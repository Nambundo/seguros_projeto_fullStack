import { useState } from 'react';
import Alert from '../components/Alert';
import Field from '../components/Field';
import SearchBox from '../components/SearchBox';
import useCrud from '../hooks/useCrud';
import useResource from '../hooks/useResource';
import { cpfMask } from '../utils/format';

const vazio = { nome: '', email: '', cpf: '' };

export default function Clientes() {
  const { items, loading, erro: erroLista, reload } = useResource('/clientes');
  const crud = useCrud({
    path: '/clientes',
    vazio,
    paraPayload: (f) => f,
    deItem: (c) => ({ nome: c.nome, email: c.email, cpf: cpfMask(c.cpf) }),
    aoMudar: reload,
  });
  const [busca, setBusca] = useState('');

  const termo = busca.trim().toLowerCase();
  const lista = items.filter(
    (c) => !termo || `${c.nome} ${c.email} ${c.cpf}`.toLowerCase().includes(termo)
  );

  return (
    <>
      <section className="card">
        <h2>{crud.editId ? `Editar cliente #${crud.editId}` : 'Novo cliente'}</h2>
        <form onSubmit={crud.enviar}>
          <Field label="Nome" erro={crud.campos.nome}>
            <input value={crud.form.nome} onChange={(e) => crud.setCampo('nome', e.target.value)} required />
          </Field>
          <Field label="E-mail" erro={crud.campos.email}>
            <input type="email" value={crud.form.email} onChange={(e) => crud.setCampo('email', e.target.value)} required />
          </Field>
          <Field label="CPF" erro={crud.campos.cpf}>
            <input value={crud.form.cpf} placeholder="000.000.000-00" onChange={(e) => crud.setCampo('cpf', e.target.value)} required />
          </Field>
          <div className="acoes-form">
            <button className="primario" disabled={crud.salvando}>{crud.salvando ? 'Salvando…' : 'Salvar'}</button>
            {crud.editId && <button type="button" onClick={crud.limpar}>Cancelar</button>}
          </div>
        </form>
        <Alert>{crud.erro}</Alert>
        <Alert tipo="ok">{crud.ok}</Alert>
      </section>

      <section className="card">
        <div className="topo-lista">
          <h2>Clientes ({lista.length})</h2>
          <SearchBox value={busca} onChange={setBusca} />
        </div>
        <Alert>{erroLista}</Alert>
        <div className="tabela-wrap">
          <table>
            <thead>
              <tr><th>ID</th><th>Nome</th><th>E-mail</th><th>CPF</th><th>Ações</th></tr>
            </thead>
            <tbody>
              {loading && <tr><td colSpan="5" className="vazio">Carregando…</td></tr>}
              {!loading && lista.length === 0 && <tr><td colSpan="5" className="vazio">Nenhum cliente encontrado.</td></tr>}
              {lista.map((c) => (
                <tr key={c.id}>
                  <td>{c.id}</td>
                  <td>{c.nome}</td>
                  <td>{c.email}</td>
                  <td>{cpfMask(c.cpf)}</td>
                  <td className="acoes">
                    <button onClick={() => crud.editar(c)}>Editar</button>
                    <button className="perigo" onClick={() => crud.excluir(c.id)}>Excluir</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
    </>
  );
}
