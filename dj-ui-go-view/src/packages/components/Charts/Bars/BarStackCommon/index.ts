import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const BarStackCommonConfig: ConfigType = {
  key: 'BarStackCommon',
  chartKey: 'VBarStackCommon',
  conKey: 'VCBarStackCommon',
  title: '多主图堆叠',
  category: ChatCategoryEnum.BAR,
  categoryName: ChatCategoryEnumName.BAR,
  package: PackagesCategoryEnum.CHARTS,
  chartFrame: ChartFrameEnum.ECHARTS,
  image: 'bar_stack.png'
}
