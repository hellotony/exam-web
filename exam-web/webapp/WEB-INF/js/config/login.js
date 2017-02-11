var config = {};
config.cssArr = [
    cssArr.login.login,
    cssArr.login.custom,
    cssArr.login.jquery_ui
];

config.jsArr = [
    jsArr.common.jquery,

    jsArr.common.base64,
    jsArr.common.json2,
    jsArr.common.system,
    jsArr.common.string,

    jsArr.my97date.WdatePicker,

    jsArr.login.login

];
JSLoader.loader(config);