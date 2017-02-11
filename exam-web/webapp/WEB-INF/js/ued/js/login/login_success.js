;
var LoginSuccess = LoginSuccess || {}
LoginSuccess = new (function(window, document){
    var _self = this;
    this.path_config = System.getContext();
    this.url_config = {
        main : _self.path_config + "/main/html"
    }

    this.init = function(frameId){
        $("#" + frameId).css("height",document.documentElement.clientHeight-40);
    }

})(window, document);
$(function () {
    $("#contentFrame").attr("src", LoginSuccess.url_config.main);
});
