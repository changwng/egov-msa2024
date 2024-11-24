import React, { useState } from 'react';
import { useRouter } from 'next/router';
import {
  Box,
  Button,
  Card,
  CardContent,
  CircularProgress,
  Typography,
  Alert,
} from '@mui/material';
import { Upload as UploadIcon } from '@mui/icons-material';
import { useUploadExcelContentsMutation } from '../../store/services/contentApi';

const ContentUploadPage: React.FC = () => {
  const router = useRouter();
  const [file, setFile] = useState<File | null>(null);
  const [uploadExcelContents, { isLoading, error }] = useUploadExcelContentsMutation();

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files[0]) {
      setFile(event.target.files[0]);
    }
  };

  const handleUpload = async () => {
    if (!file) return;

    try {
      await uploadExcelContents({ file, siteId: 1 }).unwrap();
      router.push('/contents');
    } catch (err) {
      console.error('Upload failed:', err);
    }
  };

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        엑셀 업로드
      </Typography>

      <Card sx={{ maxWidth: 600, mx: 'auto', mt: 4 }}>
        <CardContent>
          <Box sx={{ textAlign: 'center', py: 4 }}>
            <input
              accept=".xlsx,.xls"
              style={{ display: 'none' }}
              id="excel-file"
              type="file"
              onChange={handleFileChange}
            />
            <label htmlFor="excel-file">
              <Button
                variant="contained"
                component="span"
                startIcon={<UploadIcon />}
                disabled={isLoading}
              >
                엑셀 파일 선택
              </Button>
            </label>
            {file && (
              <Typography variant="body1" sx={{ mt: 2 }}>
                선택된 파일: {file.name}
              </Typography>
            )}
            {error && (
              <Alert severity="error" sx={{ mt: 2 }}>
                업로드 중 오류가 발생했습니다.
              </Alert>
            )}
          </Box>

          <Box sx={{ display: 'flex', justifyContent: 'center', mt: 2 }}>
            <Button
              variant="contained"
              color="primary"
              onClick={handleUpload}
              disabled={!file || isLoading}
              sx={{ mr: 2 }}
            >
              {isLoading ? <CircularProgress size={24} /> : '업로드'}
            </Button>
            <Button
              variant="outlined"
              onClick={() => router.push('/contents')}
              disabled={isLoading}
            >
              취소
            </Button>
          </Box>
        </CardContent>
      </Card>

      <Box sx={{ mt: 4 }}>
        <Typography variant="h6" gutterBottom>
          엑셀 파일 형식 안내
        </Typography>
        <Typography variant="body1">
          1. 엑셀 파일은 .xlsx 또는 .xls 형식이어야 합니다.
          <br />
          2. 첫 번째 행은 헤더로 사용됩니다.
          <br />
          3. 필수 항목: 제목, 내용, 콘텐츠 타입, 상태
          <br />
          4. 날짜 형식: YYYY-MM-DD
          <br />
          5. 콘텐츠 타입: HTML, TEXT, MARKDOWN
          <br />
          6. 상태: DRAFT, PUBLISHED, ARCHIVED
        </Typography>
      </Box>
    </Box>
  );
};

export default ContentUploadPage;
