import {getOnlineFromData} from '@/api/system/onlineFrom.js'
import { getToken } from '@/utils/auth'
import ThirdParty from '@/orange-party/index.js';
export default {
  mixins: [ThirdParty],
  data() {
    return {
    }
  },
  methods: {
    showAddDialog(fromId){
      return new Promise((resolve, reject) => {


        getOnlineFromData({
          "formId":fromId
        }).then(res =>{
          let onlineForm = res.data.onlineForm;
          let formConfigData = JSON.parse(onlineForm.widgetJson);
          formConfigData = formConfigData.pc;
          let formConfig = {
            rawData: res.data,
            formName: onlineForm.formName,
            formType: onlineForm.formType,
            formKind: onlineForm.formKind,
            masterTableId: onlineForm.masterTableId,
            labelWidth: formConfigData.labelWidth,
            labelPosition: formConfigData.labelPosition,
            gutter: formConfigData.gutter,
            height: formConfigData.height,
            width: formConfigData.width,
            fullscreen: formConfigData.fullscreen,
            advanceQuery: formConfigData.advanceQuery,
            widgetList: formConfigData.widgetList,
            operationList: (formConfigData.operationList || []).sort((value1, value2) => {
              return (value1.showOrder || 0) - (value2.showOrder || 0)
            }),
            tableWidget: formConfigData.tableWidget,
            leftWidget: formConfigData.leftWidget,
            customFieldList: formConfigData.customFieldList,
            formEventList: formConfigData.formEventList,
            maskFieldList: formConfigData.maskFieldList
          }

          let{param,thirdPartyOptions} = this.createParams(formConfig)


          let dlgUrl = 'http://localhost:8085/#' + thirdPartyOptions.pathName + '?appId=ruoyi&token=' + getToken() + '&dlgFullScreen=' + (thirdPartyOptions.fullscreen ? '1' : '0');
          if (param != null) {
            dlgUrl += ('&thirdParamsString=' + encodeURIComponent(JSON.stringify(param)));
          }
          let dlgOption = {
            type: 'openDialog',
            data: {
              title: formConfig.formName,
              dlgFullScreen: thirdPartyOptions.fullscreen,
              width: thirdPartyOptions.width,
              height: thirdPartyOptions.height,
              top: thirdPartyOptions.top,
              params: param,
              url: dlgUrl
            }
          }


          // this.handlerOpenDialog(dlgOption.data,JSON.parse(envt))




          // return dlgOption.data
          resolve(dlgOption.data)
          // this.showDialog(formConfig.formName,param,thirdPartyOptions)

        })
      })
    },
    createParams(formConfig){
      let dlgOptions;

      if (formConfig.fullscreen) {
        dlgOptions = {
          area: ['100vw', '100vh'],
          skin: 'fullscreen-dialog'
        };
      } else {
        dlgOptions = {
          area: [(formConfig.width ? formConfig.width : 600) + 'px', (formConfig.height ? formConfig.height : 500) + 'px']
        }
      }
      let param={
        formConfig: formConfig,
        isEdit: false,
        isCopy: false,
        readOnly: false,
        fullscreen: formConfig.fullscreen,
        path: "thirdOnlineEditForm"
      }
      let thirdPartyOptions={
        fullscreen: formConfig.fullscreen,
        width: dlgOptions.area[0],
        height: dlgOptions.area[1],
        pathName: '/thirdParty/thirdOnlineEditForm'
      }
      return {"param":param,"thirdPartyOptions":thirdPartyOptions}
    },
    showDialog (title, params, options) {
      // 调用第三方弹窗方法
      if (options && window.parent) {
        let location = window.location;
        let dlgUrl = 'http://localhost:8085/#' + options.pathName + '?appId=ruoyi&token=' + getToken() + '&dlgFullScreen=' + (options.fullscreen ? '1' : '0');
        if (params != null) {
          dlgUrl += ('&thirdParamsString=' + encodeURIComponent(JSON.stringify(params)));
        }
        let dlgOption = {
          type: 'openDialog',
          data: {
            title: title,
            dlgFullScreen: options.fullscreen,
            width: options.width,
            height: options.height,
            top: options.top,
            params: params,
            url: dlgUrl
          }
        }
        // let envt ="{\"isTrusted\":true}"
        console.log("data",dlgOption.data)

        // this.handlerOpenDialog(dlgOption.data,JSON.parse(envt))
        window.parent.postMessage(dlgOption, '*');
      }
    }

  }

}