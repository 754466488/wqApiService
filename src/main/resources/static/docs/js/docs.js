$.views.settings.allowCode(true);
$.views.converters("getRefName", function (val) {
    return getRefName(val);
});

var params = getUrlQueryParam();
var tempBody = $.templates('#temp_body');
var tempBodyRefModel = $.templates('#temp_body_ref_model');
var tempBodyType = $.templates('#temp_body_type');
var tempBodyErrorCodeDesc = $.templates('#temp_errorCodeDesc_body');

$(function () {
    $.ajax({
        url: location.origin + "/v2/api-docs?group=" + params.group,
        dataType: "json",
        type: "get",
        async: false,
        success: function (data) {
            //layui init
            layui.use(['layer', 'jquery', 'element'], function () {
                var $ = layui.jquery, layer = layui.layer, element = layui.element;
            });
            var jsonData = eval(data);

            $("#title").html(jsonData.info.title);
            $("body").html($("#template").render(jsonData));

            $("[name='a_path']").click(function () {
                var path = $(this).attr("path");
                var method = $(this).attr("method");
                var operationId = $(this).attr("operationId");
                $.each(jsonData.paths[path], function (i, d) {
                    if (d.operationId == operationId) {
                        d.path = path;
                        d.method = method;
                        $("#path-body").html(tempBody.render(d));

                        //如果没有返回值，直接跳过
                        if(!d.responses["200"].hasOwnProperty("schema")){
                            // continue
                            return true;
                        }

                        //基本类型
                        if(d.responses["200"]["schema"].hasOwnProperty("type")){
                            var model = {"type":d.responses["200"]["schema"]["type"]};
                            $("#path-body-response-model").append(tempBodyType.render(model));
                            // continue
                            return true;
                        }

                        //引用类型
                        if(d.parameters){
                            $.each(d.parameters, function (i, p) {
                                if (p["schema"]) { //自定义类型参数
                                    //补充 Data type
                                    var parameterModelName = getRefName(p["schema"]["$ref"]);
                                    p["type"] = parameterModelName;
                                    renderRefModel("path-body-request-model", jsonData, parameterModelName);
                                }
                            });
                        }
                        var modelName = getRefName(d.responses["200"]["schema"]["$ref"]);
                        renderRefModel("path-body-response-model", jsonData, modelName);
                    }
                });
            });

            //提交测试按钮
            $("[name='btn_submit']").click(function () {
                var operationId = $(this).attr("operationId");
                var parameterJson = {};
                $("input[operationId='" + operationId + "']").each(function (index, domEle) {
                    var k = $(domEle).attr("name");
                    var v = $(domEle).val();
                    parameterJson.push({k: v});
                });
            });
        }
    });

    //自定义接口说明部分
    $("[name='a_errorCodeDesc']").click(function () {
        $("#path-body").html(tempBodyErrorCodeDesc.render());
    });
});


/**
 * 渲染ref类型参数
 * @param domId 需要添加的domId
 * @param jsonData
 * @param modelName
 */
function renderRefModel(domId, jsonData, modelName) {
    if (modelName) {
        var model = jsonData.definitions[modelName];
        model.name = modelName;
        model.domId = domId;

        //修改有嵌套对象的type
        $.each(model.properties, function (i, v) {
            //required
            if(model.hasOwnProperty("required")){
                var requiedArr = model.required;
                var len = requiedArr.length;
                for(var n=0; n<len; n++) {
                    if(requiedArr[n] == i){
                        model.properties[i].required = true;
                        break;
                    }
                }
            }

            //集合类型
            if (v.items) {
                $.each(v.items, function (j, item) {
                    var typeModel = item.startsWith("#") ? getRefName(item) : item;
                    model.properties[i].type = "Array[" + typeModel + "]";
                });
            }

            //自定义对象类型（非Array）
            if (!v.type) {
                model.properties[i].type = getRefName(v["$ref"]);
            }
        });
        //如果该对象没有被渲染到页面，则渲染
        if ($("#ref-" + domId + "-" + modelName).length == 0) {
            if(domId.indexOf("request") >= 0){
                model["position"] = "request";
            }else if(domId.indexOf("response") >= 0){
                model["position"] = "response";
            }
            $("#" + domId).append(tempBodyRefModel.render(model));
        }

        //递归渲染多层对象嵌套
        $.each(model.properties, function (i, v) {
            //Array
            if (v.items) {
                $.each(v.items, function (j, item) {

                    if (item.startsWith("#")) {
                        renderRefModel(domId, jsonData, getRefName(item));
                    }
                });
            }

            //单个对象引用
            if(v.hasOwnProperty("$ref")){
                renderRefModel(domId, jsonData, getRefName(v["$ref"]));
            }
        });
    }
}

//获得模型名字
function getRefName(val) {
    if (!val) {
        return null;
    }
    return val.substring(val.lastIndexOf("/") + 1, val.length);
}

//测试按钮，获取数据
function getData(operationId) {
    var path = $("[m_operationId='" + operationId + "']").attr("path");
    //path 参数
    $("[p_operationId='" + operationId + "'][in='path']").each(function (index, domEle) {
        var k = $(domEle).attr("name");
        var v = $(domEle).val();
        if (v) {
            path = path.replace("{" + k + "}", v);
        }
    });

    //header参数
    var headerJson = {};
    $("[p_operationId='" + operationId + "'][in='header']").each(function (index, domEle) {
        var k = $(domEle).attr("name");
        var v = $(domEle).val();
        if (v) {
            headerJson[k] = v;
        }
    });

    //请求方式
    var parameterType = $("#content_type_" + operationId).val();

    //query 参数
    var parameterJson = {};
    if ("form" == parameterType) {
        $("[p_operationId='" + operationId + "'][in='query']").each(function (index, domEle) {
            var k = $(domEle).attr("name");
            var v = $(domEle).val();
            if (v) {
                parameterJson[k] = v;
            }
        });
    } else if ("json" == parameterType) {
        var str = $("#text_tp_" + operationId).val();
        try {
            parameterJson = JSON.parse(str);
        } catch (error) {
            layer.msg("" + error, {icon: 5});
            return false;
        }
    }

    //发送请求
    send(path, operationId, headerJson, parameterJson);
}


/**
 * 请求类型
 */
function changeParameterType(el) {
    var operationId = $(el).attr("operationId");
    var type = $(el).attr("type");
    $("#content_type_" + operationId).val(type);
    $(el).addClass("layui-btn-normal").removeClass("layui-btn-primary");
    if ("form" == type) {
        $("#text_tp_" + operationId).hide();
        $("#table_tp_" + operationId).show();
        $("#pt_json_" + operationId).addClass("layui-btn-primary").removeClass("layui-btn-normal");
    } else if ("json" == type) {
        $("#text_tp_" + operationId).show();
        $("#table_tp_" + operationId).hide();
        $("#pt_form_" + operationId).addClass("layui-btn-primary").removeClass("layui-btn-normal");
    }
}

/**
 * 发送请求
 * @param url 地址
 * @param operationId   operationId
 * @param header    header参数
 * @param data  data数据
 */
function send(url, operationId, header, data) {

    var type = $("[m_operationId='" + operationId + "']").attr("method");

    //是否有formData类型数据
    var hasFormData = $("[p_operationId='" + operationId + "'][in='formData']").length >= 1;

    //是否有body类型数据
    var hasBody = $("[p_operationId='" + operationId + "'][in='body']").length >= 1;

    var options = {withQuotes: true};

    //发送请求
    if (hasFormData) {
        var formData = new FormData($("#form_" + operationId)[0]);
        $.ajax({
            type: type,
            url: location.origin + url,
            headers: header,
            data: formData,
            dataType: 'json',
            cache: false,
            processData: false,
            contentType: false,
            success: function (data) {
                $("#json-response").jsonViewer(data, options);
            },
            error:function(e){
                $("#json-response").html("");
                layer.msg("" + JSON.stringify(e), {icon: 5});
            }
        });
        return;
    }

    //querystring ,将参数加在url后面
    url = appendParameterToUrl(url,data);

    //requestBody 请求
    var bodyData ;
    if (hasBody) {
        var dom = $("[p_operationId='" + operationId + "'][in='body']")[0];
        bodyData = $(dom).val();
    }
    var contentType = $("#consumes_" + operationId).text();

    $.ajax({
        type: type,
        url: url,
        headers: header,
        data: bodyData,
        dataType: 'json',
        contentType: contentType,
        success: function (data) {
            $("#json-response").jsonViewer(data, options);
        },
        error:function(e){
            $("#json-response").html("");
            layer.msg("" + JSON.stringify(e), {icon: 5});
        }
    });

}

/**
 * 给url拼装参数
 * @param url
 * @param parameter
 */
function appendParameterToUrl(url, parameter) {
    if($.isEmptyObject(parameter)){
        return url;
    }
    $.each(parameter, function (k, v) {
        if (url.indexOf("?") == -1) {
            url += "?";
        }
        url += k;
        url += "=";
        url += v;
        url += "&";
    });
    return url.substring(0, url.length - 1);
}

function getUrlQueryParam() {
    var args = {};
    var query = location.search.substring(1);
    var pairs = query.split("&");
    for (var i = 0; i < pairs.length; i++) {
        var pos = pairs[i].indexOf('=');
        if (pos == -1) continue;
        var argname = pairs[i].substring(0, pos);
        var value = pairs[i].substring(pos + 1);
        value = decodeURIComponent(value);
        args[argname] = value;
    }
    return args;
}
