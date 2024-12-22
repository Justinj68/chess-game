import React from 'react';

export const Button = ({
  children,
  className = '',
  size = 'md',
  variant = 'default', // Variants: default, primary, secondary, danger
  ...props
}) => {
  const sizeClasses = {
    sm: 'px-3 py-1 text-sm',
    md: 'px-4 py-2 text-base',
    lg: 'px-6 py-3 text-lg',
  };

  const variantClasses = {
    default: 'bg-black text-white border-gray-800 hover:bg-gray-700',
    primary: 'bg-blue-600 text-white border-blue-600 hover:bg-blue-500',
    secondary: 'bg-gray-200 text-gray-800 border-gray-300 hover:bg-gray-300',
    danger: 'bg-red-600 text-white border-red-600 hover:bg-red-500',
  };

  return (
    <button
      className={`font-medium rounded-md transition-all duration-300 focus:outline-none focus:ring-2 focus:ring-offset-2 ${sizeClasses[size]} ${variantClasses[variant]} ${className}`}
      {...props}
    >
      {children}
    </button>
  );
};
