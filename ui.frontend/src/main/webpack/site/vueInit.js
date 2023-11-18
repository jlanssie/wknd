import { createApp } from 'vue';
import Demo from '../components/Demo.vue';

const COMPONENTS = {
    "demo": Demo
}

class VueComponent extends HTMLElement {
    constructor() {
        super();
    }

    connectedCallback() {
        const props = this.dataset;
        const component = COMPONENTS[props.component];

        if (component !== undefined) {
            createApp(component, { ...props}).mount(this);
        }
    }
}

customElements.define('vue-component', VueComponent);
