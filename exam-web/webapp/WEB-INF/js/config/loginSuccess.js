var config = {};
config.cssArr = [
    cssArr.bootstrap.bootstrap_css,
    cssArr.map,
    cssArr.pagination,
    cssArr.style,
    cssArr.common.btn,
    cssArr.common.jquery_multiselect,
    cssArr.common.jquery_ui
];

config.jsArr = [
    jsArr.common.jquery,

    jsArr.common.ajaxfileupload,

    jsArr.common.base64,
    jsArr.common.json2,
    jsArr.common.system,
    jsArr.common.string,

    jsArr.common_js,
    jsArr.login_success

];
JSLoader.loader(config);