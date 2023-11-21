import React, {Suspense} from "react";
import {createRoot} from 'react-dom';
import {Demo} from "../components/Demo";

const COMPONENTS = {
    "demo": Demo
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
                <Suspense fallback={null}>
                    <Component {...props} />
                </Suspense>
            );
        }
    }
}

customElements.define('react-component', ReactComponent);