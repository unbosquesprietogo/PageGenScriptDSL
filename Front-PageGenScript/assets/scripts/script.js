const files = { "MyFirstPage.pgs": "" };
let currentFile = "MyFirstPage.pgs";
const keywords = ["page", "title", "styles", "primary-color", "secondary-color", "sections", "type", "navbar", 
                  "background-color", "items", "text", "link", "color", "hero", "subtitle", "button", 
                  "card-deck", "cards", "card", "accordion", "content", "alerts", "alert", "success", 
                  "danger", "message", "modal", "id", "exampleModal", "body", "carousel", "image", 
                  "caption", "list-group", "toast", "toastExample", "delay"];
const stringRegex = /'[^']*'/g;
const numberRegex = /\b\d+\b/g;
const symbolRegex = /[\[\],:]/g;
const commentRegex = /(^|\s)(#[^\n]*)/g;
let undoStack = [];
let redoStack = [];
function highlightSyntax() {
    saveState(); 
    const editor = document.getElementById('editor');
    let content = editor.innerText;
    // Mantén la selección y el cursor
    const selection = window.getSelection();
    const range = selection.getRangeAt(0);
    const preCursorPosition = getCaretCharacterOffsetWithin(editor);
    // Quita los eventos de input temporalmente
    editor.removeEventListener('input', highlightSyntax);
    // Aplica el resaltado
    content = content.replace(commentRegex, (match, p1, p2) => {
        return `${p1}<span class="token-comment">${p2}</span>`;
    });
    content = content.replace(stringRegex, match => `<span class="token-string">${match}</span>`);
    content = content.replace(/(<span class="token-(comment|string)">.*?<\/span>)|[^<]+/g, (match) => {
        if (match.startsWith('<span class="token-')) {
            return match;
        } else {
            return match
                .replace(numberRegex, match => `<span class="token-number">${match}</span>`)
                .replace(symbolRegex, match => `<span class="token-symbol">${match}</span>`)
                .replace(new RegExp(`\\b(${keywords.join('|')})\\b`, 'g'), match => `<span class="token-keyword">${match}</span>`);
        }
    });

    editor.innerHTML = content;
    restoreCaretPosition(editor, preCursorPosition);

    // Vuelve a activar los eventos de input
    editor.addEventListener('input', highlightSyntax);
}
function getCaretCharacterOffsetWithin(element) {
    let caretOffset = 0;
    const range = window.getSelection().getRangeAt(0);
    const preCaretRange = range.cloneRange();
    preCaretRange.selectNodeContents(element);
    preCaretRange.setEnd(range.endContainer, range.endOffset);
    caretOffset = preCaretRange.toString().length;
    return caretOffset;
}
function restoreCaretPosition(element, offset) {
    const selection = window.getSelection();
    const range = document.createRange();
    let charCount = 0, nodeStack = [element], node, foundStart = false;
    while (nodeStack.length > 0) {
        node = nodeStack.pop();
        if (node.nodeType == 3) { // Si es un nodo de texto
            const nextCharCount = charCount + node.length;
            if (!foundStart && offset >= charCount && offset <= nextCharCount) {
                range.setStart(node, offset - charCount);
                range.collapse(true);
                foundStart = true;
            }
            charCount = nextCharCount;
        } else {
            let i = node.childNodes.length;
            while (i--) {
                nodeStack.push(node.childNodes[i]);
            }
        }
    }
    selection.removeAllRanges();
    selection.addRange(range);
}
function handleKeyDown(event) {
    const editor = document.getElementById('editor');
    if (event.key === 'Enter') {
        event.preventDefault();
        insertNewLine(editor);
    } else if (event.key === 'Tab') {
        event.preventDefault();
        insertTab(editor);
    } else if (event.ctrlKey && event.key === 'z') {
        event.preventDefault();
        undo();
    } else if (event.ctrlKey && event.key === 'y') {
        event.preventDefault();
        redo();
    }
}
function insertNewLine(editor) {
    const selection = window.getSelection();
    const range = selection.getRangeAt(0);

    // Crear un nodo de salto de línea
    const br = document.createElement('br');
    range.deleteContents(); // Eliminar el contenido seleccionado, si lo hay
    range.insertNode(br); // Insertar el salto de línea

    // Mover el cursor después del salto de línea
    range.setStartAfter(br);
    range.collapse(true);

    // Restaurar la selección y posición del cursor
    selection.removeAllRanges();
    selection.addRange(range);
}

function insertTab(editor) {
    const tabSize = 2;
    const indent = ' '.repeat(tabSize);
    document.execCommand('insertText', false, indent);
    highlightSyntax();
}

function getIndentation(node) {
    const textContent = node.textContent || '';
    const match = textContent.match(/^\s*/);
    return match ? match[0] : '';
}

function saveState() {
    undoStack.push(document.getElementById('editor').innerHTML);
    redoStack = [];
}

function undo() {
    if (undoStack.length > 0) {
        redoStack.push(document.getElementById('editor').innerHTML);
        document.getElementById('editor').innerHTML = undoStack.pop();
        highlightSyntax();
        restoreCaretPosition(document.getElementById('editor'), getCaretCharacterOffsetWithin(document.getElementById('editor')));
    }
}

function redo() {
    if (redoStack.length > 0) {
        undoStack.push(document.getElementById('editor').innerHTML);
        document.getElementById('editor').innerHTML = redoStack.pop();
        highlightSyntax();
        restoreCaretPosition(document.getElementById('editor'), getCaretCharacterOffsetWithin(document.getElementById('editor')));
    }
}

async function runDSL() {
    const code = document.getElementById('editor').innerText;
    const preview = document.getElementById('preview');
    const backend = document.getElementById('backendSwitch').value;
    let url;

    if (backend === 'java') {
        url = 'http://localhost:8080/tokenizer/tokenize';
    } else {
        url = 'http://localhost:8000/tokenizer/tokenize/';
    }

    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ dsl_content: code }),
        });
    
        if (response.ok) {
            const data = await response.json();
            const formattedJSON = syntaxHighlight(JSON.stringify(data, null, 2));
            preview.innerHTML = `<pre>${formattedJSON}</pre>`;
        } else if (response.status === 400) {
            const errorData = await response.json();
            preview.innerHTML = `<pre class="token-error">${errorData.error}</pre>`;
        } else if (response.status === 500) {
            const errorData = await response.json();
            preview.innerHTML = `<pre class="token-error">${errorData.message}</pre>`;
        } else {
            preview.innerHTML = `<pre class="token-error">Error: ${response.status}</pre>`;
        }
    } catch (error) {
        preview.innerHTML = `<pre class="token-error">Error: No se pudo conectar al servidor</pre>`;
    }
}



function syntaxHighlight(json) {
    return json.replace(/"(\\u[\da-fA-F]{4}|\\[^u]|[^\\"])*"(?=\s*:)/g, '<span class="token-keyword">$&</span>') // claves
               .replace(/:\s*"[^"]*"/g, '<span class="token-string">$&</span>') // valores de cadenas
               .replace(/:\s*\d+/g, '<span class="token-number">$&</span>') // números
               .replace(/[{}[\],]/g, '<span class="token-symbol">$&</span>'); // símbolos
}

function createNewFile() {
    const fileName = prompt("Ingrese el nombre del archivo:");
    if (fileName && !files[fileName]) {
        files[fileName] = "";
        const fileList = document.getElementById('fileList');
        const li = document.createElement('li');
        li.classList.add('list-group-item');
        li.innerText = fileName;
        li.onclick = () => openFile(fileName);
        fileList.appendChild(li);
        openFile(fileName);
    }
}

function deleteFile() {
    if (confirm(`¿Estás seguro de que quieres eliminar ${currentFile}?`)) {
        delete files[currentFile];
        document.getElementById('editor').innerText = "";
        const fileList = document.getElementById('fileList');
        const items = fileList.getElementsByTagName('li');
        for (const item of items) {
            if (item.innerText === currentFile) {
                item.remove();
                break;
            }
        }
        currentFile = null;
    }
}

function openFile(fileName) {
    if (currentFile) {
        saveState(); // Guarda el estado actual antes de abrir otro archivo
        files[currentFile] = document.getElementById('editor').innerText;
    }
    currentFile = fileName;
    document.getElementById('editor').innerText = files[fileName] || "";
    saveState(); // Guarda el estado inicial del archivo abierto
    highlightSyntax();
    // Actualiza la selección del archivo activo
    const fileListItems = document.querySelectorAll('#fileList .list-group-item');
    fileListItems.forEach(item => {
        item.classList.toggle('active-file', item.innerText === fileName);
    });
}

window.onload = function () {
    const fileList = document.getElementById('fileList');
    for (const file in files) {
        const li = document.createElement('li');
        li.classList.add('list-group-item');
        li.innerText = file;
        li.onclick = () => openFile(file);
        fileList.appendChild(li);
    }

    openFile(currentFile);
}

function downloadFile() {
    if (!currentFile) {
        alert("No hay un archivo abierto para descargar.");
        return;
    }

    const content = document.getElementById('editor').innerText;
    const blob = new Blob([content], { type: 'text/plain' });
    const url = window.URL.createObjectURL(blob);
    
    const a = document.createElement('a');
    a.href = url;
    a.download = currentFile;
    document.body.appendChild(a);
    a.click();
    
    setTimeout(() => {
        window.URL.revokeObjectURL(url);
        a.remove();
    }, 0);
}

function handleFileUpload(event) {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();

    reader.onload = function(e) {
        const content = e.target.result;
        const fileName = file.name;

        if (!files[fileName]) {
            files[fileName] = content;
            const fileList = document.getElementById('fileList');
            const li = document.createElement('li');
            li.classList.add('list-group-item');
            li.innerText = fileName;
            li.onclick = () => openFile(fileName);
            fileList.appendChild(li);
        } else {
            alert("El archivo ya existe en el explorador.");
        }

        openFile(fileName);
    };

    reader.readAsText(file);
}
