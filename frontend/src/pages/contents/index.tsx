import React from 'react';
import {
  Box,
  Button,
  Card,
  CardContent,
  Grid,
  IconButton,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  TextField,
  Typography,
} from '@mui/material';
import {
  Add as AddIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  Upload as UploadIcon,
} from '@mui/icons-material';
import { useRouter } from 'next/router';

interface Content {
  id: number;
  title: string;
  contentType: string;
  status: string;
  createdAt: string;
  updatedAt: string;
}

const ContentsPage: React.FC = () => {
  const router = useRouter();
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(10);

  // 임시 데이터
  const contents: Content[] = [
    {
      id: 1,
      title: '공지사항 1',
      contentType: 'HTML',
      status: 'PUBLISHED',
      createdAt: '2024-01-01',
      updatedAt: '2024-01-01',
    },
    // 추가 데이터...
  ];

  const handleChangePage = (event: unknown, newPage: number) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  return (
    <Box>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" gutterBottom>
          콘텐츠 관리
        </Typography>
      </Box>

      <Card sx={{ mb: 4 }}>
        <CardContent>
          <Grid container spacing={2} alignItems="center">
            <Grid item xs={12} sm={4}>
              <TextField
                fullWidth
                label="검색어"
                placeholder="제목 검색"
                size="small"
              />
            </Grid>
            <Grid item>
              <Button variant="contained" color="primary">
                검색
              </Button>
            </Grid>
          </Grid>
        </CardContent>
      </Card>

      <Box sx={{ mb: 2 }}>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => router.push('/contents/create')}
          sx={{ mr: 1 }}
        >
          콘텐츠 생성
        </Button>
        <Button
          variant="outlined"
          startIcon={<UploadIcon />}
          onClick={() => router.push('/contents/upload')}
        >
          엑셀 업로드
        </Button>
      </Box>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>제목</TableCell>
              <TableCell>콘텐츠 타입</TableCell>
              <TableCell>상태</TableCell>
              <TableCell>생성일</TableCell>
              <TableCell>수정일</TableCell>
              <TableCell>작업</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {contents
              .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
              .map((content) => (
                <TableRow key={content.id}>
                  <TableCell>{content.id}</TableCell>
                  <TableCell>{content.title}</TableCell>
                  <TableCell>{content.contentType}</TableCell>
                  <TableCell>{content.status}</TableCell>
                  <TableCell>{content.createdAt}</TableCell>
                  <TableCell>{content.updatedAt}</TableCell>
                  <TableCell>
                    <IconButton
                      size="small"
                      onClick={() => router.push(`/contents/edit/${content.id}`)}
                    >
                      <EditIcon />
                    </IconButton>
                    <IconButton size="small" color="error">
                      <DeleteIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
          </TableBody>
        </Table>
        <TablePagination
          rowsPerPageOptions={[5, 10, 25]}
          component="div"
          count={contents.length}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      </TableContainer>
    </Box>
  );
};

export default ContentsPage;
