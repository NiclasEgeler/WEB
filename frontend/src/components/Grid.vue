<template>
    <v-container class="game-board" fluid fill-height>
        <v-layout class="game-row" row v-for="(row, i) in data.cells" :key="i">
            <Cell v-for="(cell, j) in row" :key="j" :x="i" :y="j" :value="cell.value" :isMine="cell.isMine"
                :isFlagged="cell.isFlagged" :isOpen="!cell.isHidden" @cell-clicked="handleCellClick" @cell-flagged="handleCellFlagged" />
        </v-layout>
    </v-container>
</template>

<script setup>
import Cell from './Cell.vue'

function handleCellClick(x, y) {
    emit('cell-clicked', x, y)
}

function handleCellFlagged(x, y) {
  emit('cell-flagged', x, y)
}

const emit = defineEmits(['cell-clicked', 'cell-flagged'])

defineProps({
    data: Object
});
</script>

<style scoped>
.game-board {
    padding: 20px;
    border-top: 6px solid #cacaca;
    border-left: 6px solid #cacaca;
    border-bottom: 6px solid #9f9f9f;
    border-right: 6px solid #9f9f9f;
    background-color: #b4b4b4;
}

@media (max-width: 600px) {
    .game-row {
        height: 35px;
    }
}
</style>
