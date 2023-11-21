import React, { lazy, Suspense } from "react";
import {createRoot} from 'react-dom';

const COMPONENTS = {
    "demo": lazy(() =>
        import(/* webpackPrefetch: true, webpackChunkName: "demo" */ "../components/Demo.jsx")
    ),
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