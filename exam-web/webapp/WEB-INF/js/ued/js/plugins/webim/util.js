var agent = window.navigator.userAgent.toLowerCase();
var sendMessageEditor = {
    // 获取iframe的window对象
    getWin: function() {
        return /*!/firefox/.test(agent)*/ false ? sendMessageEditor.iframe.contentWindow : window.frames[sendMessageEditor.iframe.name];
    },
    //获取iframe的document对象
    getDoc: function() {
        return !/firefox/.test(agent) ? sendMessageEditor.getWin().document : (sendMessageEditor.iframe.contentDocument || sendMessageEditor.getWin().document);
    },
    init: function(userJID) {
        //打开document对象，向其写入初始化内容，以兼容FireFox
        var doc = sendMessageEditor.getDoc();
        doc.open();
        var html = [
            '<html>',
            '<head><style type="text/css">body{border:0;margin:0;padding:3px;height:94%;cursor:text;background-color:white;font-size:13px;font-family:"Microsoft Yahei",Helvetica,Arial,sans-serif;}</style></head>',
            '<body jid="', userJID, '"></body>',
            '</html>'
        ].join("");
        doc.write(html);
        //打开document对象编辑模式
        doc.designMode = "on";
        doc.close();
    },
    closeOrOpenDoc: function(mode) {
        var doc = sendMessageEditor.getDoc();
        //打开document对象编辑模式
        doc.designMode = mode;
    },
    getContent: function() {
        var doc = sendMessageEditor.getDoc();
        //获取编辑器的body对象
        var body = doc.body || doc.documentElement;
        //获取编辑器的内容
        var content = body.innerHTML;
        //对内容进行处理，例如替换其中的某些特殊字符等等
        //Some code

        //返回内容
        return content;
    },
    //统一的执行命令方法
    execCmd: function(cmd, value, d) {
        var doc = d || sendMessageEditor.getDoc();
        //doc对象的获取参照上面的代码
        //调用execCommand方法执行命令
        doc.execCommand(cmd, false, value === undefined ? null : value);
    },
    getStyleState: function(cmd) {
        var doc = sendMessageEditor.getDoc();
        //doc对象的获取参考上面的对面
        //光标处是否是粗体
        var state = doc.queryCommandState(cmd);
        if (state) {
            //改变按钮的样式
        }
        return state;
    },
    insertAtCursor: function(text, d, w) {
        var doc = d || sendMessageEditor.getDoc();
        var win = w || sendMessageEditor.getWin();
        //win对象的获取参考上面的代码
        if (/msie/.test(agent)) {
            win.focus();
            var r = doc.selection.createRange();
            if (r) {
                r.collapse(true);
                r.pasteHTML(text);
            }
        } else if (/gecko/.test(agent) || /opera/.test(agent)) {
            win.focus();
            sendMessageEditor.execCmd('InsertHTML', text, doc);
        } else if (/safari/.test(agent)) {
            sendMessageEditor.execCmd('InsertText', text, doc);
        }
    }
};
var json = new function() {
    var useHasOwn = !!{}.hasOwnProperty;
    var pad = function(n) {
        return n < 10 ? "0" + n : n;
    };
    var m = {
        "\b": '\\b',
        "\t": '\\t',
        "\n": '\\n',
        "\f": '\\f',
        "\r": '\\r',
        '"': '\\"',
        "\\": '\\\\'
    };
    var encodeString = function(s) {
        if (/["\\\x00-\x1f]/.test(s)) {
            return '"' + s.replace(/([\x00-\x1f\\"])/g, function(a, b) {
                var c = m[b];
                if (c) {
                    return c;
                }
                c = b.charCodeAt();
                return "\\u00" + Math.floor(c / 16).toString(16) + (c % 16).toString(16);
            }) + '"';
        }
        return '"' + s + '"';
    };
    var encodeArray = function(o) {
        var a = ["["],
            b, i, l = o.length,
            v;
        for (i = 0; i < l; i += 1) {
            v = o[i];
            switch (typeof v) {
                case "undefined":
                case "function":
                case "unknown":
                    break;
                default:
                    if (b) {
                        a.push(',');
                    }
                    a.push(v === null ? "null" : json.decode(v));
                    b = true;
            }
        }
        a.push("]");
        return a.join("");
    };
    var encodeDate = function(o) {
        return '"' + o.getFullYear() + "-" + pad(o.getMonth() + 1) + "-" + pad(o.getDate()) + "T" + pad(o.getHours()) + ":" + pad(o.getMinutes()) + ":" + pad(o.getSeconds()) + '"';
    };
    this.decode = function(o) {
        if (typeof o == "undefined" || o === null) {
            return "null";
        } else if (o instanceof Array) {
            return encodeArray(o);
        } else if (o instanceof Date) {
            return encodeDate(o);
        } else if (typeof o == "string") {
            return encodeString(o);
        } else if (typeof o == "number") {
            return isFinite(o) ? String(o) : "null";
        } else if (typeof o == "boolean") {
            return String(o);
        } else {
            var a = ["{"],
                b, i, v;
            for (i in o) {
                if (!useHasOwn || o.hasOwnProperty(i)) {
                    v = o[i];
                    switch (typeof v) {
                        case "undefined":
                        case "function":
                        case "unknown":
                            break;
                        default:
                            if (b) {
                                a.push(',');
                            }
                            a.push(this.decode(i), ":",
                                v === null ? "null" : this.decode(v));
                            b = true;
                    }
                }
            }
            a.push("}");
            return a.join("");
        }
    };
    this.encode = function(json) {
        return eval("(" + json + ")");
    };
};
var Base64 = function() {
    var a = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
        b = {
            encode: function(b) {
                var c, d, e, f, g, h, i, j = "",
                    k = 0;
                do c = b.charCodeAt(k++), d = b.charCodeAt(k++), e = b.charCodeAt(k++), f = c >> 2, g = (3 & c) << 4 | d >> 4, h = (15 & d) << 2 | e >> 6, i = 63 & e, isNaN(d) ? h = i = 64 : isNaN(e) && (i = 64), j = j + a.charAt(f) + a.charAt(g) + a.charAt(h) + a.charAt(i); while (k < b.length);
                return j
            },
            decode: function(b) {
                var c, d, e, f, g, h, i, j = "",
                    k = 0;
                b = b.replace(/[^A-Za-z0-9\+\/\=]/g, "");
                do f = a.indexOf(b.charAt(k++)), g = a.indexOf(b.charAt(k++)), h = a.indexOf(b.charAt(k++)), i = a.indexOf(b.charAt(k++)), c = f << 2 | g >> 4, d = (15 & g) << 4 | h >> 2, e = (3 & h) << 6 | i, j += String.fromCharCode(c), 64 != h && (j += String.fromCharCode(d)), 64 != i && (j += String.fromCharCode(e)); while (k < b.length);
                return j
            }
        };
    return b
}();