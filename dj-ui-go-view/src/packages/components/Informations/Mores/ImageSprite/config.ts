import { PublicConfigClass } from '@/packages/public'
import { CreateComponentType } from '@/packages/index.d'
import { ImageSpriteConfig } from './index'
import cloneDeep from 'lodash/cloneDeep'
import demo from '@/assets/demo1.png'
export const option = {
  // 图片路径
  dataset: demo,
  // 适应方式
  width:50,//图片显示宽度
  height:50,
  // 数量
  count: 10,
  //帧数
  fps:30,
}

export default class Config extends PublicConfigClass implements CreateComponentType
{
  public key = ImageSpriteConfig.key
  public chartConfig = cloneDeep(ImageSpriteConfig)
  public option = cloneDeep(option)
}
