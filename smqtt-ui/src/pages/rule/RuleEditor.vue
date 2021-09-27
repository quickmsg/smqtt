<template>
  <div class="flow-editor">
          <VGEditor ref="flowChart" class="editor">
              <div class="editor-container">
                  <div class="editor-container__header">
                      <FlowToolbar graph-type="flow" />
                  </div>
                  <div class="editor-container__container">
                      <div class="editor-container__sidebar">
                          <FlowPanelItem />
                      </div>
                      <div class="editor-container__main">
                          <Flow
                              :data="flowData"
                              :graph="{ mode: 'readOnly' }"
                              :on-node-click="handleNodeClick"
                          />
                      </div>
                      <div class="editor-container__panel">
                          <Minimap :height="200"/>
                          <FlowPanelDetail />
                      </div>
                  </div>
              </div>
              <FlowContextMenu graph-type="flow" />
              <RegisterEdge
                  name="custom-polyline"
                  extend="flow-polyline"
                  :config="customEdgeConfig"
              />
          </VGEditor>
      </div>
</template>

<script>
import VGEditor, { Flow, RegisterEdge, Minimap } from 'vg-editor'
import FlowToolbar from '../components/editor/ToolBar'
import { FlowPanelItem } from '../components/editor/PanelItem'
import { FlowPanelDetail } from '../components/editor/PanelDetail'
import FlowContextMenu from '../components/editor/ContextMenu'
import flowData from './flow-data'

export default {
  name: 'FlowEditor',
  components: {
    VGEditor,
    Flow,
    Minimap,
    FlowToolbar,
    FlowPanelItem,
    FlowPanelDetail,
    FlowContextMenu,
    RegisterEdge
  },
  data() {
    return {
      flowData,
      customEdgeConfig: {
        getActivedStyle(item) {
          return {
            lineWidth: 3
          }
        },
        getSelectedStyle(item) {
          return {
            lineWidth: 3
          }
        }
      }
    }
  },
  methods: {
    handleNodeClick(e) {
      console.log(e)
    }
  }
}
</script>

<style lang="less">
@import "../../theme/graph-editor";
.flow-editor {
  display: flex;
  flex-direction: column;
}
</style>
