<template>
  <Toolbar class="toolbar">
    <ButtonItem
      v-for="item in buttonArr"
      :key="item.command"
      :command="item.command"
      :icon="item.icon"
      :text="item.text"
    />
  </Toolbar>
</template>

<script>
import { Toolbar } from 'vg-editor'
import ButtonItem from './ButtonItem'
import { PUBLICARR, MINDARR } from './ToolBar'

export default {
  name: 'ToolBar',
  components: {
    Toolbar,
    ButtonItem
  },
  props: {
    graphType: {
      type: String,
      required: true
    }
  },
  computed: {
    buttonArr() {
      const { graphType } = this
      if (graphType === 'mind') return MINDARR
      return PUBLICARR
    }
  }
}
</script>

<style lang="less">
.toolbar {
  display: flex;
  align-items: center;
  height: 27px;
  .command i {
    display: inline-block;
    width: 27px;
    height: 27px;
    margin: 0 8px;
    padding-top: 10px;
    text-align: center;
    cursor: pointer;
    &:hover {
      border: 1px solid #e6e9ed;
    }
  }
  .disable i {
    color: rgba(0, 0, 0, 0.25);
    cursor: auto;
    &:hover {
      border: 1px solid #fff;
    }
  }
}
</style>
