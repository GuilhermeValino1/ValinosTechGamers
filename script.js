// 1. Dados dos Serviços (CNAEs organizados)
const servicos = [
    { nome: "Software & Web", info: "Criação de lojas virtuais e sistemas customizáveis." },
    { nome: "Manutenção Tech", info: "Reparo de eletrônicos e recarga de cartuchos." },
    { nome: "Cursos & Treinos", info: "Treinamento de informática especializado." },
    { nome: "Agenciamento", info: "Intermediação de negócios e espaços publicitários." }
];

// 2. Dados dos Produtos (Comércio Varejista)
const produtos = [
    { item: "Peças Informática", preco: "Consulte", cat: "Hardware" },
    { item: "Aparelhos de Vídeo", preco: "R$ 1.200", cat: "Eletrônicos" },
    { item: "Artigos Esportivos", preco: "R$ 150", cat: "Lifestyle" },
    { item: "Livros & Criativos", preco: "R$ 80", cat: "Cultura" }
];

// Função para renderizar serviços
const listaServ = document.getElementById('grid-servicos');
servicos.forEach(s => {
    listaServ.innerHTML += `
        <div class="card">
            <h3 style="color:var(--primaria)">${s.nome}</h3>
            <p>${s.info}</p>
        </div>
    `;
});

// Função para renderizar produtos
const listaProd = document.getElementById('grid-produtos');
produtos.forEach(p => {
    listaProd.innerHTML += `
        <div class="card">
            <span style="font-size: 0.8rem; color: var(--secundaria)">${p.cat}</span>
            <h4>${p.item}</h4>
            <p style="margin: 10px 0; font-weight: bold;">${p.preco}</p>
            <button class="btn-acao" style="padding: 8px">Ver Detalhes</button>
        </div>
    `;
});

// Alerta simples no formulário
document.getElementById('orcamento-form').addEventListener('submit', (e) => {
    e.preventDefault();
    alert('Obrigado! A Valinos Tech Games entrará em contato em breve.');
});