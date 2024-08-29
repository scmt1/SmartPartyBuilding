// 弹窗 zdj 2023-03-20
import { onUnmounted, onMounted, ref } from 'vue'
import { layer } from 'vue3-layer'
import 'vue3-layer/dist/s3layer.css'
import { GlobalThemeJsonType } from '@/settings/chartThemes'
import { CreateComponentGroupType } from '@/packages/index.d'

const defaultChartSwitchData = ((itemList:any) => {
  console.log('事件参数错误！')
})

export function messageEventListener(chartSwitchData=defaultChartSwitchData) {
// 打开弹窗
  let index = 0
  const handlerOpenDialog = ((data: any, event: any) => {

    let area = [data.width || '40vw', data.height || '70vh']
    /*  if (data.dlgFullScreen) {
        area = ['100vw', '100vh']
      }*/
    const layerOptions = {
      anim: 1,
      title: data.title || false,
      // title: 'data.title||false',
      type: 2,
      skin: 'layer-dialog',
      resize: false,
      move: '.layui-layer-content',
      moveOut: true,
      shadeClose: true,
      closeBtn: data.closeBtn || true,
      area: area,
      offset: data.offset || 'auto',
      zIndex: data.zIndex || 1000,
      index: 0,
      content: data.url
    }
    index = layer.open(layerOptions)
  })

// 关闭弹窗
  const handlerCloseDialog = (() => {
    layer.close(index)
  })

  // 图表切换
  let groupList = ref([{} as CreateComponentGroupType])

  const handlerChartSwitch = ((data: any, event: any) => {
    // 判断是否传入数据id
    if (data && data.id) {
      for (let key in groupList.value) {
        //分组过滤
        if(groupList.value[key].isGroup){
          let itemList = groupList.value[key].groupList
          let isThis=false
          // 循环分组
          itemList.forEach((item:any)=>{
            //修改显示
            if(item.id==data.id){
              item.status.hide=false
              isThis=true
            }else {
              item.status.hide=true
            }
          })
          if(isThis){
            groupList.value[key].groupList=itemList
          }
        }
      }
      chartSwitchData(groupList)
    } else {
      console.log('事件参数错误！')
    }
    // console.log(group)
  })


  // 信息切换
  const handlerMessage = ((type: any, data: any, event: any) => {
    switch (type) {
      // 打开弹窗
      case 'openDialog':
        handlerOpenDialog(data, event)
        break
      // 关闭弹窗
      case 'closeDialog':
        handlerCloseDialog()
        break
      // 图表切换
      case 'chartSwitch':
        handlerChartSwitch(data, event)
        break
    }
  })

  // 事件监听
  const eventListener = ((e: any) => {

    handlerMessage(e.data.type, e.data.data, e)
  })

  // 初始化监听
  onMounted(() => {
    window.addEventListener('message', eventListener, false)
  })
  // 销毁监听
  onUnmounted(() => {
    window.removeEventListener('message', eventListener)
  })
  return {
    groupList,
    chartSwitchData
  }

}

// end
/*
window.parent.postMessage({
  type:"openDialog",//openDialog,closeDialog
  data:{
    width:"40vw",
    height:"70vh",
    closeBtn:false,
    // title:"",//不要此参数 默认为false
    offset:['100px', '50px'],
    zIndex:10,
    url:"http://localhost:3000/#/chart/preview/793392531453906944"
  }
}, '*');

window.parent.postMessage({
  type:"closeDialog",//关闭最近一次的弹窗
}, '*');

window.parent.postMessage({
  type:"chartSwitch",
  data:{
    id:""
  }
}, '*');

*/
