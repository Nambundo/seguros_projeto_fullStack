import { useState } from 'react';
import api, { mensagemDeErro } from '../api/api';

/**
 * Estado e ações de um formulário CRUD (criar / editar / excluir).
 *  - vazio:       valores iniciais do formulário
 *  - paraPayload: converte o form no JSON enviado à API
 *  - deItem:      converte um item da API em valores do form (edição)
 *  - aoMudar:     chamado após salvar/excluir (normalmente recarrega a lista)
 */
export default function useCrud({ path, vazio, paraPayload, deItem, aoMudar }) {
  const [form, setForm] = useState(vazio);
  const [editId, setEditId] = useState(null);
  const [salvando, setSalvando] = useState(false);
  const [erro, setErro] = useState('');
  const [campos, setCampos] = useState({});
  const [ok, setOk] = useState('');

  const setCampo = (nome, valor) => setForm((f) => ({ ...f, [nome]: valor }));

  function limpar() {
    setForm(vazio);
    setEditId(null);
    setErro('');
    setCampos({});
  }

  async function enviar(e) {
    e.preventDefault();
    setSalvando(true);
    setErro('');
    setCampos({});
    setOk('');
    try {
      const corpo = paraPayload(form);
      if (editId) await api.put(`${path}/${editId}`, corpo);
      else await api.post(path, corpo);
      setOk(editId ? 'Registro atualizado com sucesso.' : 'Registro criado com sucesso.');
      setForm(vazio);
      setEditId(null);
      if (aoMudar) aoMudar();
    } catch (err) {
      const m = mensagemDeErro(err);
      setErro(m.mensagem);
      setCampos(m.campos);
    } finally {
      setSalvando(false);
    }
  }

  function editar(item) {
    setEditId(item.id);
    setForm(deItem(item));
    setErro('');
    setCampos({});
    setOk('');
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  async function excluir(id) {
    if (!window.confirm('Confirma a exclusão deste registro?')) return;
    setErro('');
    setOk('');
    try {
      await api.delete(`${path}/${id}`);
      setOk('Registro excluído.');
      if (editId === id) limpar();
      if (aoMudar) aoMudar();
    } catch (err) {
      setErro(mensagemDeErro(err).mensagem);
    }
  }

  return { form, setCampo, editId, salvando, erro, campos, ok, enviar, editar, excluir, limpar };
}
