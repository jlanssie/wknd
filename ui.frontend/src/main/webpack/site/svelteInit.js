
import Demo from '../components/Demo.svelte';

const COMPONENTS = {
    "demo": Demo
}

class SvelteComponent extends HTMLElement {
    constructor() {
        super();
    }

    connectedCallback() {
        const props = this.dataset;
        const Component = COMPONENTS[props.component];

        new Component({ target: this, props });
    }
}

customElements.define('svelte-component', SvelteComponent);