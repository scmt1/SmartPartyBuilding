<template>
  <div class="m-video" :style="`width: ${width}px; height: ${height}px;`">
    <div style="position: absolute;color: white;left: 5px">{{ title }} </div>
    <video
      ref="veo"
      :style="`object-fit: ${zoom};`"
      :src="videoUrl"
      :poster="videoCover"
      :width="width"
      :height="height"
      :autoplay="autoplay"
      :controls="!originPlay&&controls"
      :loop="loop"
      :muted="autoplay || muted"
      :preload="preload"
      @click.prevent.once="onPlay">
      您的浏览器不支持video标签。
    </video>
    <svg v-show="originPlay || showPlay" :class="[vplay ? 'hidden' : 'u-play']" :style="`width: ${playWidth}px; height: ${playWidth}px;`" viewBox="0 0 24 24">
      <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M4.75 6.75C4.75 5.64543 5.64543 4.75 6.75 4.75H17.25C18.3546 4.75 19.25 5.64543 19.25 6.75V17.25C19.25 18.3546 18.3546 19.25 17.25 19.25H6.75C5.64543 19.25 4.75 18.3546 4.75 17.25V6.75Z"></path>
      <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M15.25 12L9.75 8.75V15.25L15.25 12Z"></path>
    </svg>
  </div>
</template>
<script>
export default {
  name: 'Video',
  props: {
    title: {
      type: String,
      required: true,
      default: null
    },
    videoUrl: { // 视频文件url，必传，支持网络地址 https 和相对地址 require('@/assets/files/Bao.mp4')
      type: String,
      required: true,
      default: null
    },
    videoCover: { // 视频封面url，支持网络地址 https 和相对地址 require('@/assets/images/Bao.jpg')
      type: String,
      default: null
    },
    width: { // 视频播放器宽度
      type: Number,
      default: 800
    },
    height: { // 视频播放器高度
      type: Number,
      default: 450
    },
    /*
      由于目前在最新版的Chrome浏览器（以及所有以Chromium为内核的浏览器）中，
      已不再允许自动播放音频和视频。就算你为video或audio标签设置了autoplay
      属性也一样不能自动播放！
      本组件解决方法：设置视频 autoplay 时，自动将视频设置为静音 muted: true 即可实现自
      动播放，然后用户可以使用控制栏开启声音，类似某宝商品自动播放的宣传视频逻辑
    */
    autoplay: { // 视频就绪后是否马上播放
      type: Boolean,
      default: false
    },
    controls: { // 是否向用户显示控件，比如进度条，全屏
      type: Boolean,
      default: true
    },
    loop: { // 视频播放完成后，是否循环播放
      type: Boolean,
      default: false
    },
    muted: { // 是否静音
      type: Boolean,
      default: false
    },
    preload: { // 是否在页面加载后载入视频，如果设置了autoplay属性，则preload将被忽略；
      type: String,
      default: 'auto' // auto:一旦页面加载，则开始加载视频; metadata:当页面加载后仅加载视频的元数据 none:页面加载后不应加载视频
    },
    showPlay: { // 播放暂停时是否显示播放器中间的暂停图标
      type: Boolean,
      default: false
    },
    playWidth: { // 中间播放暂停按钮的边长
      type: Number,
      default: 96
    },
    zoom: { // video的poster默认图片和视频内容缩放规则
      type: String,
      default: 'none' // none:(默认)保存原有内容，不进行缩放; fill:不保持原有比例，内容拉伸填充整个内容容器; contain:保存原有比例，内容以包含方式缩放; cover:保存原有比例，内容以覆盖方式缩放
    }
  },
  data () {
    return {
      originPlay: true,
      vplay: false
    }
  },
  mounted () {
    if (this.showPlay) {
      this.$refs.veo.addEventListener('pause', this.onPause)
      this.$refs.veo.addEventListener('playing', this.onPlaying)
    }
    if (this.autoplay) {
      this.vplay = true
      this.originPlay = false
    }
    // 自定义设置播放速度
    // this.$refs.veo.playbackRate = 2
  },
  methods: {
    onPlay () {
      console.log('click')
      this.vplay = true
      this.originPlay = false
      this.$refs.veo.play()
    },
    onPause () {
      this.vplay = false
      console.log('pause')
      this.$once('hook:beforeDestroy', function () {
        removeEventListener('pause', this.onPause)
      })
    },
    onPlaying () {
      this.vplay = true
      console.log('playing')
      // 自动清理自己，避免内存泄漏
      this.$once('hook:beforeDestroy', function () {
        removeEventListener('playing', this.onPlaying)
      })
    }
  }
}
</script>
<style lang="less" scoped>
.m-video {
  display: inline-block;
  position: relative;
  background: #000;
  cursor: pointer;
  &:hover {
    .u-play {
      opacity: 0.9;
    }
  }
  .u-play {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    margin: auto;
    fill: none;
    color: #FFF;
    pointer-events: none;
    opacity: 0.7;
    transition: opacity .3s;
  }
  .hidden {
    display: none;
  }
}
</style>
