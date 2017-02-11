var config = {};
config.cssArr = [
    cssArr.bootstrap.bootstrap_css,
    cssArr.xenon.select2.select2,
    cssArr.map,
];

config.jsArr = [
    jsArr.common.jquery,
    jsArr.xenon.jquery,
    jsArr.common.jquery_ui,
    jsArr.xenon.bootstrap,
    jsArr.xenon.xenon_toggles,

    jsArr.common.jquery_multiselect,
    jsArr.common.jquery_multiselect_cn,

    jsArr.common.selectbar,

    jsArr.common.string,

    jsArr.common.jquery_jqpagination,

    jsArr.my97date.WdatePicker,

    jsArr.common.base64,
    jsArr.common.json2,
    jsArr.common.system,
    jsArr.common.string,

    jsArr.xenon.select2.select2,

    jsArr.common_js,
    jsArr.map
];
JSLoader.loader(config);