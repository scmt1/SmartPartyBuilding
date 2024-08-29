import { getToken } from '@/utils/auth'
import { oneaccessLogin } from  '@/api/login'
import { encrypt,sm4Dncrypt } from "@/utils/encrypt"
import $ from 'jquery'
import './index.css'
window.jQuery = $

const layer = require('layui-layer')

export default {
  data() {
    return {
      dialogMap: new Map()
    }
  },
  methods: {
    // 发送消息
    postMessage(sender, type, data) {
      if (sender != null && type != null) {
        sender.postMessage({
          type,
          data
        }, '*')
      }
    },
    // 打开弹窗
    handlerOpenDialog(data, event) {
      const this_ = this
      let area = [data.width || '40vw', data.height || '70vh']
      if (data.dlgFullScreen) {
        area = ['100vw', '100vh']
      }
      const layerOptions = {
        title: data.title,
        type: 2,
        skin: data.dlgFullScreen ? 'fullscreen-dialog' : 'layer-dialog',
        resize: false,
        area: area,
        offset: data.dlgFullScreen ? undefined : (data.top || '50px'),
        zIndex: data.zIndex || 1000,
        index: 0,
        content: data.url,
        success: function(res, index) {
          this_.dialogMap.set(index, {
            source: event.source
          })
          var iframe = $(res).find('iframe')
          if (data.dlgFullScreen) iframe[0].style.height = '100vh'
          this_.postMessage(iframe[0].contentWindow, 'dialogIndex', index)
        }
      }
      layer.open(layerOptions)
    },
    // 关闭弹窗
    handlerCloseDialog(data) {
      if (data != null) {
        layer.close(data.index)
        const dialog = this.dialogMap.get(data.index)
        if (dialog && dialog.source) {
          this.postMessage(dialog.source, 'refreshData', data)
        }
        this.dialogMap.delete(data)
      }
    },
    // 刷新token
    handlerRefreshToken(data, event) {
      this.postMessage(event.source, 'setToken', {
        token: getToken()
      })
    },
    // 通知消息，例如成功、错误通知等
    handlerUIMessage(data, event) {
      this.$message[data.type](data.text)
    },
    // 打开新页面
    openNewPage(event,router){
      let routeUrl = this.$router.resolve({
        path: "/onLineApplyFor",
        query: {newSrc:encrypt(process.env.VUE_APP_YSZJ_ADDRESS+event.data.url)}
      });
      let path = routeUrl.href
      if(router==1){
        path = event.data.path
      }

      let employee = this.$cache.local.get("portal_employee")
      let isEnterprise = this.$cache.local.get("isEnterprise")
      if(!getToken()){
        this.$confirm('您还未登录系统，请先登录系统？', '提示', {
          confirmButtonText: '登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          oneaccessLogin(path)
        }).catch(() => {});
      }else if(isEnterprise==0&&!employee){
        this.$confirm('您还未绑定企业，请先绑定企业？', '提示', {
          confirmButtonText: '绑定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // this.$router.push({path:"/portalCenter",query:{titleIndex: 6,index:1}})
          oneaccessLogin(path,false,true)
        }).catch(() => {});

      }else{
        if(router==1){
          this.$router.push({ path: path });
        }else{
          // window.open(routeUrl.href, '_blank');
          window.location.href=path;
        }
      }



    },
    // 初始化高度
    initHeight(event){
      // if (event.origin !== 'https://rcyszj-test.lzjczl.com') return;
      const iframe = this.$refs.myIframe;
      console.log(event.data)
      // iframe.style.height = event.data.height + 'px';
      iframe.style.height = (iframe.contentWindow.document.body.scrollHeight+100) + 'px';

    },


    handlerMessage(type, data, event) {
      switch (type) {
        // 打开弹窗
        case 'openDialog':
          this.handlerOpenDialog(data, event)
          break
        // 关闭弹窗
        case 'closeDialog':
          this.handlerCloseDialog(data, event)
          break
        // 刷新token
        case 'refreshToken':
          this.handlerRefreshToken(data, event)
          break
        // 通知消息，例如成功、错误通知等
        case 'message':
          this.handlerUIMessage(data, event)
          break
        //打开新页面
        case 'openNewPage':
          this.openNewPage(event)
          break
        // 跳转内部页
        case 'toPath':
          this.openNewPage(event,1)
          break

      }
    },
    eventListener(e) {
      // if(e.origin !== process.env.VUE_APP_YSZJ_ADDRESS )return;// 可以根据需要设置正确的域名
      if (e.data == null) return
      if(!e.data.type||e.data.type=='height'){
        this.initHeight(e);
      }
      this.handlerMessage(e.data.type, e.data.data, e)
    }
  },
  created() {
    window.addEventListener('message', this.eventListener, false)
  },
  destroyed() {
    window.removeEventListener('message', this.eventListener)
  }
}
