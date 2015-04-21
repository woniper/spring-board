/**
 * Created by woniper on 15. 4. 9..
 */

document.write("<script src='../..//static/js/utils/base64.min.js'></script>");

function accountEncode(username, password) {
    var text = username + ":" + password;
    return Base64.encode(text);
};

function getHttpBasicHeader(username, password) {
    var strEncode = accountEncode(username, password);
    return "Autorization: Basic " + strEncode;
};