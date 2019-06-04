import React, {MutableRefObject, ReactElement, useRef, useState} from 'react';
import classNames from "classnames";
import {StyleableComponentProps} from "../../types/StyleableComponentProps";
import styles from './dropdown.module.css';

interface Props extends StyleableComponentProps {
    activeClassName?: string,
    trigger: ReactElement
    content: ReactElement
}

type DivType = HTMLDivElement;
type RefType = MutableRefObject<DivType>

const Dropdown: React.FC<Props> = ({ className, activeClassName, trigger: Trigger, content: Content}) => {
    const [collapsed, setCollapsed] = useState(true);
    const contentRef = useRef<DivType>(null) as RefType;

    const show = (element: HTMLElement) => {
        element.style.height = element.scrollHeight + 'px';

        let listener: EventListener;
        listener = () => {
            element.removeEventListener('transitionend', listener);
            element.style.height = null;
            setCollapsed(false);
        };

        element.addEventListener('transitionend', listener);
    };

    const hide = (element: HTMLElement) => {
        const sectionHeight = element.scrollHeight;
        const elementTransition = element.style.transition;
        element.style.transition = '';

        requestAnimationFrame(() => {
            element.style.height = sectionHeight + 'px';
            element.style.transition = elementTransition;
            requestAnimationFrame(() => element.style.height = 0 + 'px' );
        });

        setCollapsed(true)
    };

    const handleClick = () =>
        collapsed ? show(contentRef.current) : hide(contentRef.current);

    return (
      <div className={className}>
          <button className={styles.button} type="button" onClick={handleClick}>
              <div className={classNames(styles.trigger, !collapsed && activeClassName)}>
                  {Trigger}
              </div>
          </button>
          <div ref={contentRef} className={styles.content} style={{ height: 0 }}>
              {Content}
          </div>
      </div>
    );
};

export default Dropdown;