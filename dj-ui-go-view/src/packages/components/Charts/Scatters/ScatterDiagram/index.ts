import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const ScatterDiagramConfig: ConfigType = {
  key: 'ScatterDiagram',
  chartKey: 'VScatterDiagram',
  conKey: 'VCScatterDiagram',
  title: '关系图',
  category: ChatCategoryEnum.SCATTER,
  categoryName: ChatCategoryEnumName.SCATTER,
  package: PackagesCategoryEnum.CHARTS,
  chartFrame: ChartFrameEnum.ECHARTS,
  image: 'diagram.png'
}
