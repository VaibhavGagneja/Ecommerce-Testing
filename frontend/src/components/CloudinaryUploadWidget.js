// src/components/CloudinaryUploadWidget.js
import React, { useRef, useState } from 'react';
import api from '../utils/api';

const CloudinaryUploadWidget = ({ onUpload, buttonText = "Upload Images", multiple = true }) => {
    const fileInputRef = useRef(null);
    const [uploading, setUploading] = useState(false);

    const handleFileChange = async (event) => {
        const files = event.target.files;
        if (!files || files.length === 0) return;

        setUploading(true);

        try {
            // Upload files one by one to our backend endpoint which handles the Cloudinary upload
            for (let i = 0; i < files.length; i++) {
                const formData = new FormData();
                formData.append('image', files[i]);
                
                const response = await api.post('/upload/image', formData, {
                    headers: { 'Content-Type': 'multipart/form-data' }
                });
                
                if (response.data && response.data.url) {
                    onUpload(response.data.url);
                }
            }
        } catch (error) {
            console.error('Error uploading image:', error);
            alert('Failed to upload image. Please check your connection and try again.');
        } finally {
            setUploading(false);
            // Reset input so the same file can be selected again if needed
            if (fileInputRef.current) {
                fileInputRef.current.value = '';
            }
        }
    };

    const triggerFileSelect = () => {
        if (fileInputRef.current) {
            fileInputRef.current.click();
        }
    };

    return (
        <>
            <input
                type="file"
                ref={fileInputRef}
                style={{ display: 'none' }}
                onChange={handleFileChange}
                accept="image/*"
                multiple={multiple}
            />
            <button
                type="button"
                onClick={triggerFileSelect}
                disabled={uploading}
                className="rounded-xl bg-orange-500 px-4 py-2 text-sm font-black text-white shadow-sm transition hover:bg-orange-600 disabled:cursor-wait disabled:opacity-50"
            >
                {uploading ? 'Uploading...' : buttonText}
            </button>
        </>
    );
};

export default CloudinaryUploadWidget;
