!function (d) {
    var a = d.document, e = {};
    $(d).on("load", function () {
    });
    var c = e.util = {
        parseOptions: function (f) {
        }, pageScroll: function () {
        }(), localStorage: function () {
        }(), sessionStorage: function () {
        }(), serialize: function (f) {
        }, deserialize: function (f) {
        }
    };

    function b(f) {
    }

    $.fn.emulateTransitionEnd = function (f) {
    };
    if (typeof define === "function") {
        define(e)
    } else {
        d.YDUI = e
    }
}(window);
!function (c) {
    function b(d, e) {
        this.$element = $(d);
        this.options = $.extend({}, b.DEFAULTS, e || {});
        this.init();
        this.bindEvent();
        this.transitioning = false
    }

    b.TRANSITION_DURATION = 150;
    b.DEFAULTS = {nav: ".tab-nav-item", panel: ".tab-panel-item", activeClass: "tab-active"};
    b.prototype.init = function () {
        var e = this, d = e.$element;
        e.$nav = d.find(e.options.nav);
        e.$panel = d.find(e.options.panel)
    };
    b.prototype.bindEvent = function () {
        var d = this;
        d.$nav.each(function (f) {
            $(this).on("click.ydui.tab", function () {
                d.open(f)
            })
        })
    };
    b.prototype.open = function (f) {
        var e = this;
        f = typeof f == "number" ? f : e.$nav.filter(f).index();
        var d = e.$nav.eq(f);
        e.active(d, e.$nav);
        e.active(e.$panel.eq(f), e.$panel, function () {
            d.trigger({type: "opened.ydui.tab", index: f});
            e.transitioning = false
        })
    };
    b.prototype.active = function (f, e, i) {
        var g = this, h = g.options.activeClass;
        var d = e.filter("." + h);

        function j() {
            typeof i == "function" && i()
        }

        f.one("webkitTransitionEnd", j).emulateTransitionEnd(b.TRANSITION_DURATION);
        d.removeClass(h);
        f.addClass(h)
    };

    function a(e) {
        var d = Array.prototype.slice.call(arguments, 1);
        return this.each(function () {
            var h = this, f = $(h), g = f.data("ydui.tab");
            if (!g) {
                f.data("ydui.tab", (g = new b(h, e)))
            }
            if (typeof e == "string") {
                g[e] && g[e].apply(g, d)
            }
        })
    }

    $(c).on("load.ydui.tab", function () {
        $("[data-ydui-tab]").each(function () {
            var d = $(this);
            d.tab(c.YDUI.util.parseOptions(d.data("ydui-tab")))
        })
    });
    $.fn.tab = a
}(window);