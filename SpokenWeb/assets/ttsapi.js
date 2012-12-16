function jsOrgShamitWebReaderStartTTS() {
	//alert( document.URL );
	//OrgShamitWebReaderAPI.showToast(document.URL);
	//alert(document.URL);
	OrgShamitWebReaderAPI.setCurrentUrl(document.URL);
	OrgShamitWebReaderAPI.speakText(jsOrgShamitWebReaderGetPageText());
}

//
// Hat Tip : http://stackoverflow.com/questions/1879477/get-web-page-text-via-javascript
//
function jsOrgShamitWebReaderGetBodyText(win) {
    var doc = win.document, body = doc.body, selection, range, bodyText;
    if (body.createTextRange) {
        return body.createTextRange().text;
    } else if (win.getSelection) {
        selection = win.getSelection();
        range = doc.createRange();
        range.selectNodeContents(body);
        selection.addRange(range);
        bodyText = selection.toString();
        selection.removeAllRanges();
        return bodyText;
    }
}

function jsOrgShamitWebReaderGetPageText() {
	return  jsOrgShamitWebReaderGetBodyText(window);
	
}

jsOrgShamitWebReaderStartTTS();