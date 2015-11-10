/**
 * Created by woniper on 2015. 11. 10..
 */
function isLogin() {
    var isSession = false;
    $.ajax({
        type : "GET",
        url : "/web-session",
        async : false
    }).done(function(data) {
        isSession = data.session;
    });
    return isSession;
}