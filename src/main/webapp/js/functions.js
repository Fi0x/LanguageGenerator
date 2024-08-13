function searchFunction() {
    var input, filter, table, rows, td, i, txtValue;
    input = document.getElementById("searchText");
    filter = input.value.toUpperCase();
    table = document.getElementById("searchableTable");
    rows = table.getElementsByTagName("tr");

    for (i = 0; i < rows.length; i++) {
        td = rows[i].getElementsByTagName("td")[0];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                rows[i].style.display = "";
            } else {
                rows[i].style.display = "none";
            }
        }
    }
}

function updateSaveState(originalWord, text, isAlreadySaved, listIndex) {
    if (originalWord !== text || !isAlreadySaved) {
        document.getElementById("deleteButton" + listIndex).style.visibility = 'hidden';
        document.getElementById("saveButton" + listIndex).style.visibility = 'visible';
    }
}

function updateRealState(newState) {
    document.getElementsByClassName("onlyFictionalLanguageOption").disabled = newState;
}