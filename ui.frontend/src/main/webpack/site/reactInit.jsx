import React from "react";
import {createRoot} from 'react-dom';

import {DemoReact} from "../components";

/* Map an AEM Component to a React Component
*
* KEY: AEM Component attribute data-component
* VALUE: React.js Component
* */
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
            createRoot(this).render(<Component {...props}/>);
        }
    }
}

customElements.define('react-component', ReactComponent);