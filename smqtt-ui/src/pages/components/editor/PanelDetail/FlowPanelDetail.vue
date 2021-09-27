<template>
  <div class="flow-panel-detail">
    <detail-panel>
      <template v-slot="{ status }">
        <node-panel :status="status">
          <detail-form type="node" />
        </node-panel>
        <edge-panel :status="status">
          <detail-form type="edge" />
        </edge-panel>
        <group-panel :status="status">
          <detail-form type="group" />
        </group-panel>
        <multi-panel :status="status" />
        <canvas-panel :status="status" />
      </template>
    </detail-panel>
  </div>
</template>

<script>
import { NodePanel, EdgePanel, GroupPanel, MultiPanel, CanvasPanel, DetailPanel } from 'vg-editor'
import DetailForm from './DetailForm'

export default {
  name: 'FlowPanelDetail',
  components: {
    NodePanel,
    EdgePanel,
    GroupPanel,
    MultiPanel,
    CanvasPanel,
    DetailPanel,
    DetailForm
  }
}
</script>

<style lang="less">
.flow-panel-detail {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 220px;
  margin-bottom: 35px;
  border-bottom: 1px solid #e6f7ff;
}
</style>
