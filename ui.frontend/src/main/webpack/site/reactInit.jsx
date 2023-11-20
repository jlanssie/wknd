import React from "react";
import {createRoot} from 'react-dom';
import {DemoReact} from "../components/Demo.jsx";

const COMPONENTS = {
    "demo": DemoReact
}

class ReactComponent extends HTMLElement {
    constructor() {
        super();
    }

    connectedCallback() {
        const props = this.dataset;
        const Component = COMPONENTS[props.component];

        if (Component !== undefined) {
            createRoot(this).render(
                <Component {...props} />
            );
        }
    }
}

customElements.define('react-component', ReactComponent);