<template>
<div v-show="ready" class="ad" :style="{'z-index': zIndex, 'left': left + 'px', 'top': top + 'px', 'display': 'block'}" @mouseover="onHover(0)" @mouseleave="onHover(1)">
  <div class="close" v-if="showTitle">
    <div class="opacity" :style="{opacity: headFilter, filter: 'alpha(opacity = ' + headFilter * 100 + ')'}"></div>
    <div class="text">
      <div class="title">{{title}}</div>
      <div class="button" :style="{background: 'url(' + closedIcon + ') no-repeat'}" @click="close">
      </div>
    </div>
  </div>
  <div>
    <a :href="linkUrl" target="_blank" title=""><img :src="img" :style="{height: imgHeight, width: imgWidth}"></a>
  </div>
</div>
</template>

<script>
export default {
  props: {
    ad: {
      type: Object,
      required: true
    }
  },
  computed: {
    headFilter() { // 头部背景透明度
      return this.ad.headFilter || 0
    },
    img() {
      return this.ad.img || ''
    },
    imgHeight() {
      return `${(this.ad.imgHeight || 0)}px`
    },
    imgWidth() {
      return `${(this.ad.imgWidth || 0)}px`
    },
    linkUrl() {
      return this.ad.linkUrl || ''
    },
    zIndex() {
      return this.ad['z-index'] || 1
    },
    title() {
      return this.ad.title || ''
    },
    closedIcon() { // 关闭按键图片
      return this.ad['closed-icon'] || ''
    }
  },
  data() {
    return {
      ready: false,
      xFlag: 0,
      yFlag: 0,
      left: 0,
      top: 0,
      width: 0,
      height: 0,
      xPos: 0,
      yPos: 0,
      yOn: 0,
      xOn: 0,
      yPath: 0,
      xPath: 0,
      hOffset: 0,
      wOffset: 0,
      interval: 0,
      delay: 50,
      step: 10,

      showTitle: false
    }
  },
  mounted() {
    this.init();
  },
  methods: {
    initDefaults() {
    },
    init() {
      this.width = document.body.clientWidth;
      this.height = document.body.clientHeight;
      this.wOffset = this.$el.clientWidth;
      this.hOffset = this.$el.clientHeight;
      this.xPos = this.getRandomNum(0, this.width - this.wOffset);
      this.yPos = this.getRandomNum(0, 300);
      this.xOn = this.getRandomNum(0, 1);
      this.yOn = this.getRandomNum(0, 1);
      this.xPath = this.getRandomNum(0, 1);
      this.yPath = this.getRandomNum(0, 1);

      const sx = this.scrollX();
      const sy = this.scrollY();
      this.left = this.xPos + sx;
      this.top = this.yPos + sy;
      this.interval = setInterval(this.intervalFn, this.delay)
      setTimeout(() => {
        this.ready = true;
      }, this.delay)
    },
    intervalFn() {
      const { step, width, height } = this;
      if (!this.wOffset || !this.hOffset) {
        this.wOffset = this.$el.clientWidth;
        this.hOffset = this.$el.clientHeight;
        this.xPos = this.getRandomNum(0, this.width - this.wOffset);
      }
      const sx = this.scrollX();
      const sy = this.scrollY();
      if (this.xFlag === sx && this.yFlag === sy) {
        this.left = this.xPos + sx;
        this.top = this.yPos + sy;

        this.yPos = this.yOn ? (this.yPos + step) : (this.yPos - step);

        if (this.yPos <= 0) {
          this.yOn = 1;
          this.yPos = 0;
        }
        // console.log(this.ad['z-index'])
        if (this.yPos >= (height - this.hOffset)) {
          this.yOn = 0;
          this.yPos = height - this.hOffset;
        }
        this.xPos = this.xOn ? this.xPos + step : this.xPos - step;

        if (this.xPos <= 0) {
          this.xOn = 1;
          this.xPos = 0;
        }

        if (this.xPos >= (width - this.wOffset)) {
          this.xOn = 0;
          this.xPos = width - this.wOffset;
        }
      }

      // console.log(this.xPos, this.yPos);

      this.xFlag = this.$el.scrollLeft;
      this.yFlag = this.$el.scrollTop;
    },
    scrollX() {
      return window.pageXOffset || (this.$el &&  this.$el.scrollLeft) || document.body.scrollLeft;
    },
    scrollY() {
      return window.pageXOffset || (this.$el &&  this.$el.scrollTop) || document.body.scrollTop;
    },
    getRandomNum(Min, Max) {
      const range = Max - Min;
      const rand = Math.random();
      return (Min + Math.round(rand * range));
    },
    onHover(type) {
      if (type === 0) {
        this.showTitle = true;
        clearInterval(this.interval);
        this.interval = null;
      } else {
        this.showTitle = false;
        this.interval = setInterval(this.intervalFn, this.delay);
      }
    },
    close() {
      this.ready = false;
      this.ad.onClose(this.ad);
    }
  }
}
</script>

<style>
.floatingAd .ad {
	z-index: 100;
	background: none;
	position: absolute;
	display: none;
}

.floatingAd a {
	color: #000000;
	display: inline-block;
	text-decoration: none;
}

.floatingAd a img {
	border: 0;
}

.floatingAd .opacity {
	position: absolute;
	top: 0;
	width: 100%;
	height: 25px;
	background-color: #000000;
	opacity: 0.20;
	filter: alpha(opacity=20);
  z-index: 1;
}

.opacity1 {
	opacity: 0.90;
	filter: alpha(opacity=90);
}

.floatingAd .text {
	position: absolute;
	top: 0;
	width: 100%;
	height: 25px;
	color: #000000;
	line-height: 25px;
  z-index: 2;
}

.floatingAd .text .button {
	position: relative;
	float: right;
	top: 5px;
	right: 5px;
	width: 16px;
	height: 16px;
	background: url("../../views/image/close.png") no-repeat;
	cursor: pointer;
}

.floatingAd .text .title {
	position: relative;
	float: left;
	font-size: 12px;
	margin-left: 5px;
}
</style>
