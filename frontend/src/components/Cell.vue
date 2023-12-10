<template>
    <v-col :class="[
        'cell',
        { 'mine': isMine && isOpen, 'open': isOpen, 'flagged': isFlagged },
        { ['cell-value-' + value]: isOpen }
    ]" @click="handleClick" @contextmenu.prevent="toggleFlag">
        <template v-if="!isOpen && !isFlagged">
            <span>?</span>
        </template>
        <template v-if="isOpen && !isMine">
            <span>{{ value }}</span>
        </template>
        <template v-if="isOpen && isMine">
            <span>💣</span>
        </template>
        <template v-if="isFlagged">
            <span>🚩</span>
        </template>
    </v-col>
</template>

<script>
export default {
    props: {
        isMine: {
            type: Boolean,
            default: false
        },
        isOpen: {
            type: Boolean,
            default: false
        },
        isFlagged: {
            type: Boolean,
            default: false
        },
        value: {
            type: Number,
            default: 0
        },
        x: {
            type: Number,
            default: 0
        },
        y: {
            type: Number,
            default: 0
        },
    },
    methods: {
        handleClick() {
            if(!this.isOpen) {
                this.$emit('cell-clicked', this.x, this.y);
            }
        },
        toggleFlag() {
            if(!this.isOpen) {             
                this.$emit('cell-flagged', this.x, this.y);
            }
        }
    }
}
</script>

<style>
.cell {
    justify-content: center;
    text-align: center;
    align-items: center;
    border-top: 4px solid white;
    border-left: 4px solid white;
    border-bottom: 4px solid lightslategray;
    border-right: 4px solid lightslategray;
    background-color: lightgray;
    color: #333;
    font-size: 30px;
    font-weight: bold;
    cursor: pointer;
}

.open {
    border: 4px solid lightgray;
}

.flagged {
    color: orange;
}

.mine {
    background-color: #ff4d4d;
    border: 1px solid #ff4d4d;
}

.cell-value-1 {
    color: blue;
}

.cell-value-2 {
    color: green;
}

.cell-value-3 {
    color: red;
}

.cell-value-4 {
    color: darkblue;
}

.cell-value-5 {
    color: darkred;
}

.cell-value-6 {
    color: darkgreen;
}

@media (max-width: 1200px) {
    .cell {
        font-size: 25px;
    }
}

@media (max-width: 992px) {
    .cell {
        font-size: 20px;
    }
}

@media (max-width: 768px) {
    .cell {
        font-size: 15px;
    }
}

@media (max-width: 600px) {
    .cell {
        font-size: 10px;
        padding-top: 6px !important;
    }
}
</style>