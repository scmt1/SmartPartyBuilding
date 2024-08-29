import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const BarDisplayConfig: ConfigType = {
  key: 'BarDisplay',
  chartKey: 'VBarDisplay',
  conKey: 'VCBarDisplay',
  title: '动态时间渐变柱形图',
  category: ChatCategoryEnum.BAR,
  categoryName: ChatCategoryEnumName.BAR,
  package: PackagesCategoryEnum.CHARTS,
  chartFrame: ChartFrameEnum.ECHARTS,
  image: 'bar_display.png'
}
