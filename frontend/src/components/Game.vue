<template>
    <v-container class="game-container" fluid fill-height>
        <Grid @cell-clicked="handleCellClick" @cell-flagged="handleCellFlagged" :data="data"></Grid>
        <v-row class="buttons">
            <v-col cols="3" md="3">
                <v-btn @click="undo" id="undoButton" color="primary">Undo</v-btn>
            </v-col>
            <v-col cols="3" md="3">
                <v-btn @click="redo" id="redoButton" color="primary">Redo</v-btn>
            </v-col>
        </v-row>
    </v-container>
</template>
  
<script setup>
import { onMounted, ref } from 'vue';
import Grid from './Grid.vue';

const data = ref([]);


async function undo() {
    const response = await fetch('http://localhost:9000/api/undo');
    data.value = await response.json();
}

async function redo() {
    const response = await fetch('http://localhost:9000/api/redo');
    data.value = await response.json();
}

async function handleCellClick(x, y) {
    console.log('Cell clicked', x, y);
    const response = await fetch(`http://localhost:9000/api/place/${x}/${y}`);
    data.value = await response.json();
}

async function handleCellFlagged(x, y) {
    console.log('Cell flagged', x, y);
    const response = await fetch(`http://localhost:9000/api/flag/${x}/${y}`);
    data.value = await response.json();
}

onMounted(async () => {
    const response = await fetch('http://localhost:9000/api/grid');
    data.value = await response.json();
});
</script>
  
<style>
.game-container {
    width: 800px;
}

.buttons{
    margin-top: 10px;
}

@media (max-width: 1200px) {
    .game-container {
        width: 750px;
    }
}

@media (max-width: 992px) {
    .game-container {
        width: 690px;
    }
}

@media (max-width: 768px) {
    .game-container {
        width: 580px;
    }
}

@media (max-width: 600px) {
    .game-container {
        width: 380px;
    }
}
</style>