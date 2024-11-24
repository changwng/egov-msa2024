import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

interface Content {
  id: number;
  title: string;
  content: string;
  contentType: string;
  templateId: string;
  status: string;
  publishStartDate: string;
  publishEndDate: string;
  metaTitle: string;
  metaDescription: string;
  metaKeywords: string;
  version: number;
  isUse: boolean;
  createdAt: string;
  updatedAt: string;
}

interface ContentListResponse {
  content: Content[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

interface ContentSearchParams {
  page?: number;
  size?: number;
  title?: string;
  status?: string;
}

export const contentApi = createApi({
  reducerPath: 'contentApi',
  baseQuery: fetchBaseQuery({ baseUrl: '/api/v1' }),
  tagTypes: ['Content'],
  endpoints: (builder) => ({
    getContents: builder.query<ContentListResponse, ContentSearchParams>({
      query: (params) => ({
        url: '/contents',
        params,
      }),
      providesTags: ['Content'],
    }),

    getContentById: builder.query<Content, number>({
      query: (id) => `/contents/${id}`,
      providesTags: ['Content'],
    }),

    createContent: builder.mutation<Content, Partial<Content>>({
      query: (content) => ({
        url: '/contents',
        method: 'POST',
        body: content,
      }),
      invalidatesTags: ['Content'],
    }),

    updateContent: builder.mutation<Content, { id: number; content: Partial<Content> }>({
      query: ({ id, content }) => ({
        url: `/contents/${id}`,
        method: 'PUT',
        body: content,
      }),
      invalidatesTags: ['Content'],
    }),

    deleteContent: builder.mutation<void, number>({
      query: (id) => ({
        url: `/contents/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: ['Content'],
    }),

    uploadExcelContents: builder.mutation<Content[], { file: File; siteId: number }>({
      query: ({ file, siteId }) => {
        const formData = new FormData();
        formData.append('file', file);
        return {
          url: `/contents/excel-upload/${siteId}`,
          method: 'POST',
          body: formData,
        };
      },
      invalidatesTags: ['Content'],
    }),
  }),
});

export const {
  useGetContentsQuery,
  useGetContentByIdQuery,
  useCreateContentMutation,
  useUpdateContentMutation,
  useDeleteContentMutation,
  useUploadExcelContentsMutation,
} = contentApi;
