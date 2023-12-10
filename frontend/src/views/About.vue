<template>
    <v-container>
        <v-row justify="center">
            <v-col cols="12">
                <h1 class="display-4 text-center mt-5">Welcome to Minesweeper!</h1>
            </v-col>
        </v-row>
        <v-row>
            <v-col cols="12" md="6">
                <h2>How to Play</h2>
                <p class="lead">
                    Minesweeper is a classic single-player puzzle game. The objective is to clear
                    the playing area without detonating any mines. Here's how to play:
                </p>
                <v-list>
                    <v-list-item>Click on any square to start the game.</v-list-item>
                    <v-list-item>
                        If the clicked square is not a mine, it will reveal a number indicating how
                        many mines are adjacent to it.
                    </v-list-item>
                    <v-list-item>If the clicked square is a mine, you lose the game.</v-list-item>
                    <v-list-item>
                        Use the numbers as clues to determine the location of mines and click on
                        squares you think are safe. If a square has no adjacent mines, it will
                        automatically expand to reveal surrounding squares.
                    </v-list-item>
                    <v-list-item>
                        Your goal is to clear all non-mine squares without clicking on any mines.
                        You can mark suspected mine locations with flags to help keep track.
                    </v-list-item>
                </v-list>
            </v-col>
            <v-col cols="12" md="6">
                <h2>History</h2>
                <p class="lead">
                    Minesweeper has a rich history dating back to the 1960s. It gained worldwide
                    popularity when it was included in the Microsoft Windows operating system in
                    the early 1990s. The game was created by Curt Johnson and was inspired by a
                    mainframe computer game called "Relentless" developed by Robert Donner in the
                    early 1980s.
                </p>
                <p class="lead">
                    Over the years, Minesweeper has become an iconic puzzle game and is loved by
                    players of all ages.
                </p>
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup>
// Vue 3 script setup
</script>