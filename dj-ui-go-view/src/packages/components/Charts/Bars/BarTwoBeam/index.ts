import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const BarTwoBeamConfig: ConfigType = {
  key: 'BarTwoBeam',
  chartKey: 'VBarTwoBeam',
  conKey: 'VCBarTwoBeam',
  title: '双光柱图',
  category: ChatCategoryEnum.BAR,
  categoryName: ChatCategoryEnumName.BAR,
  package: PackagesCategoryEnum.CHARTS,
  chartFrame: ChartFrameEnum.ECHARTS,
  image: 'beam_two.png'
}
