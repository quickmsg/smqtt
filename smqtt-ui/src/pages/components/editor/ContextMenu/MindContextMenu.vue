<template>
  <context-menu class="contextMenu">
    <node-menu>
      <menu-li command="append" text="Topic" />
      <menu-li command="appendChild" icon="append-child" text="Subtopic" />
      <menu-li command="collapse" text="Fold" />
      <menu-li command="expand" text="Unfold" />
      <menu-li command="delete" />
    </node-menu>
    <canvas-menu>
      <menu-li command="undo" />
      <menu-li command="redo" />
    </canvas-menu>
  </context-menu>
</template>

<script>
import { NodeMenu, CanvasMenu, ContextMenu } from 'vg-editor'
import MenuLi from './MenuItem'

export default {
  name: 'FlowContextMenu',
  components: {
    NodeMenu,
    CanvasMenu,
    ContextMenu,
    MenuLi
  }
}
</script>

<style lang="less">
.contextMenu {
  color: #1f1f1f;
  display: none;
  overflow: hidden;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  .command.disable {
    .item {
      color: rgba(0, 0, 0, 0.25);
      cursor: auto;
      &:hover {
        background: #fff;
      }
    }
  }
  .item {
    display: flex;
    align-items: center;
    padding: 5px 12px;
    cursor: pointer;
    transition: all 0.3s;
    user-select: none;
    &:hover {
      background: #e6f7ff;
    }
    i {
      margin-right: 8px;
    }
  }
}
</style>
