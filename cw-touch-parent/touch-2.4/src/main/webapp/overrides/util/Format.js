/**
 *
 */
Ext.define('Ext.overrides.util.Format', {
    override: 'Ext.util.Format',

    defaultDateFormat: 'd.m.y',

    numberFormat: {
        decimalSeparator: '.',
        decimalPrecision: 2,
        groupingSeparator: ',',
        groupingSize: 3
        // currencySymbol : '$'
    },
    bankAccount: function (v) {
        if (Ext.isEmpty(v) || v.length != 20) return 'не корректный номер';
        return v.substr(0, 5) + ' ' + v.substr(5, 3) + ' ' + v.substr(8, 4) + ' ' + v.substr(12, 4) + ' ' + v.substr(16, 4);
    },
    number: function (value, numberFormat) {
        var format = Ext.apply(Ext.apply({}, Ext.util.Format.numberFormat), numberFormat);
        if (typeof value !== 'number') {
            value = String(value);
            if (format.currencySymbol) {
                value = value.replace(format.currencySymbol, '');
            }
            if (format.groupingSeparator) {
                value = value.replace(new RegExp(format.groupingSeparator, 'g'), '');
            }
            if (format.decimalSeparator !== '.') {
                value = value.replace(format.decimalSeparator, '.');
            }
            value = parseFloat(value);
        }
        var neg = value < 0;
        value = Math.abs(value).toFixed(format.decimalPrecision);
        var i = value.indexOf('.');
        if (i >= 0) {
            if (format.decimalSeparator !== '.') {
                value = value.slice(0, i) + format.decimalSeparator + value.slice(i + 1);
            }
        } else {
            i = value.length;
        }
        if (format.groupingSeparator) {
            while (i > format.groupingSize) {
                i -= format.groupingSize;
                value = value.slice(0, i) + format.groupingSeparator + value.slice(i);
            }
        }
        if (format.currencySymbol) {
            value = format.currencySymbol + value;
        }
        if (neg) {
            value = '-' + value;
        }
        return value;
    },
    money: function (v, tpl) {
        if (!v) return "-";
        if (v === '') return v;

        v = Math.round((v - 0) * 100) / 100;
        v = (v == Math.floor(v)) ? v + ".00" : (v * 10 == Math.floor(v * 10)) ? v + "0" : v;
        v = String(v);
        var ps = v.split(".");
        var whole = ps[0];
        var sub = ps[1] ? "." + ps[1] : ".00";
        var r = /(\d+)(\d{3})/;
        while (r.test(whole)) {
            whole = whole.replace(r, "$1,$2");
        }
        v = whole + sub;
        if (v.charAt(0) == "-") {
            v = "-" + v.substr(1);
        }
        if (tpl) {
            v = Ext.String.format(tpl, v);
        } else if (Ext.util.Format.numberFormat.currencySymbol) {
            v = v + " " + Ext.util.Format.numberFormat.currencySymbol;
        }
        return v;
    }
});
