function saveInDb(lang1, word1, button, word1Num) {
    button.style.visibility = 'hidden';

    let lang2 = document.getElementById("languageSelectionDropDown").value;
    let word2 = document.getElementById("translationWord").value;
    let translationDto = {
        languageId: lang1,
        word: word1,
        translatedLanguageId: lang2,
        translatedWord: word2
    }

    fetch(`${baseUrl}/translation/save`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(translationDto)
    }).then(response => {
        if (!response.ok)
            throw `Could not save translation (${response.status})`;
        response.json().then(responseJson => {
            addTranslationRow(responseJson.languageId, responseJson.languageName, responseJson.word, responseJson.wordNumber, lang1, word1Num);
            button.style.visibility = 'visible';
        });
    }).catch(errorResponse => {
        alert(errorResponse);
        button.style.visibility = 'visible';
    });
}

function deleteFromDb(lang1, word1, lang2, word2, index, button) {
    button.style.visibility = 'hidden';

    let translationDto = {
        languageId: lang1,
        wordNumber: word1,
        translatedLanguageId: lang2,
        translatedWordNumber: word2
    }

    fetch(`${baseUrl}/translation/delete`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(translationDto)
    }).then(response => {
        if (!response.ok)
            throw `Could not delete translation (${response.status})`;

        let row = document.getElementById(`translationRow${index}`);
        row.parentNode.removeChild(row);

    }).catch(errorResponse => {
        alert(errorResponse);
        button.style.visibility = 'visible';
    });
}

function addTranslationRow(languageId, languageName, word, wordNumber, originalLang, originalWord) {
    let tbody = document.getElementById("translationTableBody");
    let row = document.createElement("tr");
    let lastRowNumber = tbody.childElementCount > 0 ? tbody.lastElementChild.id.replace("translationRow", "") : -1;
    row.id = `translationRow${lastRowNumber + 1}`;
    let td1 = document.createElement("td");
    let lblWord = document.createElement("label");
    lblWord.innerText = word;
    td1.appendChild(lblWord);
    row.appendChild(td1);
    let td2 = document.createElement("td");
    let languageButton = document.createElement("a");
    languageButton.href = `dictionary?languageId=${languageId}`;
    languageButton.classList.add("btn-hidden-light");
    languageButton.innerText = languageName;
    td2.appendChild(languageButton);
    row.appendChild(td2);
    let td3 = document.createElement("td");
    let translationsButton = document.createElement("a");
    translationsButton.href = `word?languageId=${languageId}&word=${word}`;
    translationsButton.classList.add("btn");
    translationsButton.innerText = "Translations";
    td3.appendChild(translationsButton);
    row.appendChild(td3);
    let td4 = document.createElement("td");
    let deleteButton = document.createElement("a");
    deleteButton.onclick = () => {
        deleteFromDb(languageId, wordNumber, originalLang, originalWord, lastRowNumber + 1, deleteButton);
    };
    deleteButton.classList.add("btn");
    deleteButton.classList.add("btn-danger");
    deleteButton.innerText = "Delete Translation";
    td4.appendChild(deleteButton);
    row.appendChild(td4);
    tbody.appendChild(row);
}