import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const BarCrossrangeAConfig: ConfigType = {
  key: 'BarCrossrangeA',
  chartKey: 'VBarCrossrangeA',
  conKey: 'VCBarCrossrangeA',
  title: '横向柱状图类型A',
  category: ChatCategoryEnum.BAR,
  categoryName: ChatCategoryEnumName.BAR,
  package: PackagesCategoryEnum.CHARTS,
  chartFrame: ChartFrameEnum.ECHARTS,
  image: 'bar_y_A.png'
}
