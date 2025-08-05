function saveInDb(loopIndex, languageId, button) {
    button.style.visibility = 'hidden';

    let word = document.getElementById(`wordText${loopIndex}`).value;
    let wordDto = {
        word: `${word}`,
        languageId: languageId,
    }

    fetch(`${baseUrl}/word/save`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(wordDto)
    }).then(response => {
        if (!response.ok)
            throw `Could not save word '${word}' (${response.status})`;
        response.json().then(responseJson => {
            wordIdMapping[loopIndex] = {
                text: word,
                dbNumber: responseJson
            };
            document.getElementById(`deleteButton${loopIndex}`).style.visibility = 'visible';
        });
    }).catch(errorResponse => {
        alert(errorResponse);
        button.style.visibility = 'visible';
    });
}

function deleteFromDb(loopIndex, languageId, button, deleteRow = false) {
    button.style.visibility = 'hidden';

    let word;
    if (deleteRow)
        word = wordIdMapping[loopIndex].word;
    else
        word = document.getElementById(`wordText${loopIndex}`).value;
    let wordDto = {
        languageId: languageId,
        wordNumber: wordIdMapping[loopIndex].dbNumber
    }

    fetch(`${baseUrl}/word/delete`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(wordDto)
    }).then(response => {
        if (!response.ok)
            throw `Could not delete word '${word}' (${response.status})`;

        wordIdMapping[loopIndex] = {
            text: word,
            dbNumber: -1
        };
        if (deleteRow) {
            let row = document.getElementById(`wordRow${loopIndex}`);
            row.parentNode.removeChild(row);
        } else
            document.getElementById(`saveButton${loopIndex}`).style.visibility = 'visible';
    }).catch(errorResponse => {
        alert(errorResponse);
        button.style.visibility = 'visible';
    });
}