<template>
  <div class="mind-panel-detail">
    <detail-panel>
      <template v-slot="{ status }">
        <node-panel :status="status">
          <detail-form type="node" />
        </node-panel>
        <canvas-panel :status="status" />
      </template>
    </detail-panel>
  </div>
</template>

<script>
import { NodePanel, CanvasPanel, DetailPanel } from 'vg-editor'
import DetailForm from './DetailForm'

export default {
  name: 'MindPanelDetail',
  components: {
    NodePanel,
    CanvasPanel,
    DetailPanel,
    DetailForm
  }
}
</script>

<style lang="less">
.mind-panel-detail {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 230px;
  margin-bottom: 35px;
  border-bottom: 1px solid #e6f7ff;
}
</style>
